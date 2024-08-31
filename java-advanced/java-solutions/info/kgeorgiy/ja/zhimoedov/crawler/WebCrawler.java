package info.kgeorgiy.ja.zhimoedov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Crawls websites with many threads.
 *
 * @author denchicez
 */
public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService tasksDownloaders;
    private final ExecutorService tasksExtractors;

    /**
     * Creates a new downloader storing documents in specified directory.
     *
     * @param downloader  downloads document from the Web
     * @param downloaders how many threads can install
     * @param extractors  how many threads can extractors links from Document
     * @param perHost     how threads install from one host in moment
     */
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.tasksDownloaders = Executors.newFixedThreadPool(downloaders);
        this.tasksExtractors = Executors.newFixedThreadPool(extractors);
    }


    /**
     * Downloads website up to specified depth.
     *
     * @param url   start <a href="http://tools.ietf.org/html/rfc3986">URL</a>.
     * @param depth download depth.
     * @return download result.
     */
    @Override
    public Result download(String url, int depth) {
        TaskDownload taskDownloader = new TaskDownload();
        return taskDownloader.runDownload(url, depth);
    }

    public class TaskDownload {
        private final Set<String> wasDownload = new ConcurrentSkipListSet<>();
        private final Set<String> resList = new ConcurrentSkipListSet<>();
        private final Queue<String> urlsLayer = new ConcurrentLinkedQueue<>();
        private final Map<String, IOException> errors = new ConcurrentHashMap<>();
        Phaser phaser;
        Queue<String> layerQueue = new ConcurrentLinkedQueue<>();

        public Result runDownload(String url, int depth) {
            urlsLayer.add(url);
            // :NOTE: Phaser надо создавать здесь
            while (!urlsLayer.isEmpty()) {
                // :NOTE: не надо Phaser создавать на каждой итерации цикла
                phaser = new Phaser(1);
                while (!urlsLayer.isEmpty()) {
                    String urlQueue = urlsLayer.poll();
                    if (!(urlQueue == null || wasDownload.contains(urlQueue))) {
                        wasDownload.add(urlQueue);
                        int finalDepth = depth;
                        phaser.register();
                        tasksDownloaders.submit(() -> downloadFileTask(urlQueue, finalDepth));
                    }
                }
                phaser.arriveAndAwaitAdvance();
                urlsLayer.addAll(layerQueue);
                layerQueue.clear();
                depth -= 1;
            }
            return new Result(resList.stream().toList(), errors);
        }

        private void downloadFileTask(String urlQueue, int depth) {
            try {
                Document document = downloader.download(urlQueue);
                resList.add(urlQueue);
                if (depth > 1) {
                    phaser.register();
                    tasksExtractors.submit(() -> executorLinksTask(document));
                }
            } catch (IOException e) {
                errors.put(urlQueue, e);
            } finally {
                phaser.arrive();
            }
        }

        private void executorLinksTask(Document document) {
            try {
                List<String> links = document.extractLinks();
                layerQueue.addAll(links);
            } catch (IOException e) {
                // ???
            } finally {
                phaser.arrive();
            }
        }
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(0, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(0, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Closes this web-crawler, relinquishing any allocated resources.
     */
    @Override
    public void close() {
        tasksExtractors.shutdown();
        tasksDownloaders.shutdown();
//        shutdownAndAwaitTermination(tasks/**/Extractors);
//        shutdownAndAwaitTermination(tasksDownloaders);
    }


    public static void main(String[] args) {
        if (args == null || args.length == 0 || args[0] == null || args.length > 5) {
            System.err.println("WebCrawler must be have url. Format need be " +
                    "WebCrawler url [depth [downloads [extractors [perHost]]]]");
            return;
        }
        String url = args[0];
        int depth, downloads, extractors, perHost;
        try {
            depth = 1 < args.length ? Integer.parseInt(args[1]) : 2;
            downloads = 2 < args.length ? Integer.parseInt(args[2]) : 2;
            extractors = 3 < args.length ? Integer.parseInt(args[3]) : 2;
            perHost = 4 < args.length ? Integer.parseInt(args[4]) : 2;
        } catch (NumberFormatException e) {
            // :NOTE: e.getMessage()
            System.err.println("params must be number: " + e.getMessage());
            return;
        }
        try (Crawler crawler = new WebCrawler(new CachingDownloader(10), downloads,
                extractors, perHost)) {
            Result result = crawler.download(url, depth);
            System.err.println(result.getDownloaded());
            System.err.println(result.getErrors());
        } catch (IOException e) {
            System.err.println("Smth wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

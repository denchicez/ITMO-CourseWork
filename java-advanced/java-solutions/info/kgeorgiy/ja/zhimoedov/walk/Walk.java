package info.kgeorgiy.ja.zhimoedov.walk;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Walk {
    private static final String badHash = "0".repeat(64);

    private static String bytesToHex(byte[] hash) {
        BigInteger bi = new BigInteger(1, hash);
        return String.format("%0" + (hash.length << 1) + "x", bi);
    }

    public static String getHash256Path(Path path) {
        byte[] buffer = new byte[4096];
        try (InputStream bufferedReader = Files.newInputStream(path)) {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            int howMuchReadBytes;
            while ((howMuchReadBytes = bufferedReader.read(buffer)) >= 0) {
                hash.update(buffer, 0, howMuchReadBytes);
            }
            return bytesToHex(hash.digest());
        } catch (FileNotFoundException e) {
            System.err.println("File not found with :-" + path);
        } catch (IOException e) {
            System.err.println("Problem with file :-" + path);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Can't find SHA-256 :-" + path);
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument exception:- " + path);
        } catch (SecurityException e) {
            System.err.println("Security exception warning- " + path);
        } catch (UnsupportedOperationException e) {
            System.err.println("Unsupported operation exception " + path);
        } catch (NullPointerException e) {
            System.err.println("Null Pointer exception " + path);
        } catch (FileSystemNotFoundException e) {
            System.err.println("File system not found exception " + path);
        }
        return badHash;
    }

    public static void walk(Path input, Path output) {
        try (BufferedReader fileInput = Files.newBufferedReader(input)) {
            try (BufferedWriter fileOutput = Files.newBufferedWriter(output)) {
                String path;
                while ((path = fileInput.readLine()) != null) {
                    String hash256ForPath;
                    try {
                        hash256ForPath = getHash256Path(Path.of(path));
                    } catch (InvalidPathException e) {
                        hash256ForPath = badHash;
                    }
                    fileOutput.write(hash256ForPath + " " + path);
                    fileOutput.newLine();
                }
            } catch (NoSuchFileException e) {
                System.err.println("Not found output file " + e);
            } catch (IOException e) {
                System.err.println("Have problem with output file");
            } catch (IllegalArgumentException e) {
                System.err.println("Illegal argument exception with output file " + e);
            } catch (SecurityException e) {
                System.err.println("Security exception warning with output file " + e);
            } catch (UnsupportedOperationException e) {
                System.err.println("Unsupported operation exception with output file " + e);
            } catch (FileSystemNotFoundException e) {
                System.err.println("File system not found exception with output file " + e);
            }
        } catch (NoSuchFileException e) {
            System.err.println("Not found with input file " + e);
        } catch (IOException e) {
            System.err.println("Have problem with input file " + e);
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument exception with input file " + e);
        } catch (SecurityException e) {
            System.err.println("Security exception warning with input file " + e);
        } catch (UnsupportedOperationException e) {
            System.err.println("Unsupported operation exception with input file " + e);
        } catch (FileSystemNotFoundException e) {
            System.err.println("File system not found exception with input file " + e);
        }
    }

    public static void createDirsIfNeed(Path output) {
        Path parent;
        if ((parent = output.getParent()) != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                System.err.println("Can't create directory");
            }
        }
    }

    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Arguments must be NOT null");
        } else if (args.length != 2) {
            System.err.println("Need two arguments");
        } else if (args[0] == null) {
            System.err.println("First argument can't be null");
        } else if (args[1] == null) {
            System.err.println("Second argument can't be null");
        } else {
            try {
                Path input = Path.of(args[0]);
                try {
                    Path output = Path.of(args[1]);
                    createDirsIfNeed(output);
                    walk(input, output);
                } catch (InvalidPathException e) {
                    System.err.println("Can't get path from second arg");
                }
            } catch (InvalidPathException e) {
                System.err.println("Can't get path from first arg");
            }
        }
    }
}
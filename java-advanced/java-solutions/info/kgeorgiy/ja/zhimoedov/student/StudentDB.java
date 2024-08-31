package info.kgeorgiy.ja.zhimoedov.student;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {
    static private final Comparator<Student> NAME_COMPARATOR = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .reversed().thenComparingInt(Student::getId);

    private static <T> List<T> getFieldStudents(final List<Student> students,
                                                final Function<Student, T> getField) {
        return students.stream().map(getField).toList();
    }

    private static <T, R> R getSortedFilterCollect(final Collection<Student> students,
                                                   final Comparator<Student> comparator,
                                                   final T predicate,
                                                   final Function<Student, T> mapper,
                                                   final Collector<Student, ?, R> collector) {
        return students.stream()
                .sorted(comparator)
                .filter(student -> predicate == null || predicate.equals(mapper.apply(student)))
                .collect(collector);
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getFieldStudents(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getFieldStudents(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getFieldStudents(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getFieldStudents(students, Student -> Student.getFirstName() + " " + Student.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().max(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return getSortedFilterCollect(students, Comparator.comparingInt(Student::getId),
                null, null, Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return getSortedFilterCollect(students, NAME_COMPARATOR, null, null, Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return getSortedFilterCollect(students, NAME_COMPARATOR, name, Student::getFirstName, Collectors.toList());

    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return getSortedFilterCollect(students, NAME_COMPARATOR, name, Student::getLastName, Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return getSortedFilterCollect(students, NAME_COMPARATOR, group, Student::getGroup, Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByGroup(students, group).stream().collect(
                Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
//        return getSortedFilterCollect(students, NAME_COMPARATOR, group, Student::getGroup,
//                Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }
}
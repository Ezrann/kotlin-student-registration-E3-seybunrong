//Main.kt

import java.util.Scanner

val students = mutableListOf<Student>()
val courses = mutableListOf<Course>()
val enrollments = mutableListOf<Enrollment>()
val scanner = Scanner(System.`in`)

fun main() {
    while (true) {
        showMenu()
        print("Enter choice (1-7): ")
        when (scanner.nextLine()) {
            "1" -> registerStudent()
            "2" -> createCourse()
            "3" -> enrollStudentInCourse()
            "4" -> viewCourseStudents()
            "5" -> viewAllStudents()
            "6" -> viewAllCourses()
            "7" -> {
                println("Goodbye!")
                return
            }

            else -> println("Invalid option. Try again.")
        }
    }
}

fun registerStudent() {
    println("---------- NEW STUDENT REGISTRATION ----------")
    print("Enter Student Name: ")
    val name = scanner.nextLine()
    print("Enter Email (optional): ")
    val email = scanner.nextLine().ifBlank { null }
    print("Enter Major: ")
    val major = scanner.nextLine()
    val id = "ST${(students.size + 1).toString().padStart(3, '0')}"
    students.add(Student(id, name, email, major))
    println("Student $name registered successfully! ID: $id")
}

fun createCourse() {
    println("---------- CREATE A NEW COURSE ----------")
    print("Enter Course ID (e.g., CS101): ")
    val courseId = scanner.nextLine()
    print("Enter Course Name: ")
    val courseName = scanner.nextLine()
    print("Enter Course Credits: ")
    val credits = scanner.nextLine().toIntOrNull() ?: 0
    courses.add(Course(courseId, courseName, credits))
    println("Course $courseName created successfully!")
}

fun enrollStudentInCourse() {
    print("Enter Student ID to enroll: ")
    val studentId = scanner.nextLine()
    val student = students.find { it.id == studentId }
    if (student == null) {
        println("Student not found!")
        return
    }

    print("Enter Course ID to enroll in: ")
    val courseId = scanner.nextLine()
    val course = courses.find { it.courseId == courseId }
    if (course == null) {
        println("Course not found!")
        return
    }

    if (enrollments.any { it.studentId == studentId && it.courseId == courseId }) {
        println("Student is already enrolled in this course.")
        return
    }

    enrollments.add(Enrollment(studentId, courseId))
    println("${student.name} enrolled in ${course.courseName} successfully!")
}

fun viewCourseStudents() {
    print("Enter Course ID to view students: ")
    val courseId = scanner.nextLine()
    val course = courses.find { it.courseId == courseId }
    if (course == null) {
        println("Course not found!")
        return
    }

    val enrolledStudents = enrollments
        .filter { it.courseId == courseId }
        .mapNotNull { e -> students.find { it.id == e.studentId } }

    if (enrolledStudents.isEmpty()) {
        println("No students enrolled in ${course.courseName}.")
        return
    }

    println("\n--- Students in ${course.courseName} ---")
    enrolledStudents.forEach {
        println("ID: ${it.id}, Name: ${it.name}, Email: ${it.email ?: "N/A"}, Major: ${it.major}")
    }
}

fun viewAllStudents() {
    if (students.isEmpty()) {
        println("No students registered yet.")
        return
    }
    println("\n--- Registered Students ---")
    students.forEach {
        println("ID: ${it.id}, Name: ${it.name}, Email: ${it.email ?: "N/A"}, Major: ${it.major}")
    }
}

fun viewAllCourses() {
    if (courses.isEmpty()) {
        println("No courses available.")
        return
    }
    println("\n--- All Courses ---")
    courses.forEach { course ->
        val count = enrollments.count { it.courseId == course.courseId }
        println("ID: ${course.courseId}, Name: ${course.courseName}, Credits: ${course.credits}, Enrolled Students: $count")
    }
}
fun showMenu() {
    println("\n=== Student Course Registration System ===")
    println("1. Register New Student")
    println("2. Create New Course")
    println("3. Enroll Student in Course")
    println("4. View Students in a Course")
    println("5. View All Students")
    println("6. View All Courses")
    println("7. Exit")
}

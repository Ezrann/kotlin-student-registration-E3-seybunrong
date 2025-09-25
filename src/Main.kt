
import kotlin.system.exitProcess

data class Student(
    val id: String,
    val name: String,
    val email: String?,
    val major: String
)

data class Course(
    val courseId: String,
    val courseName: String,
    val credits: Int
)

data class Enrollment(
    val studentId: String,
    val courseId: String
)

// --- Main Program ---
fun main() {
    val students = mutableListOf<Student>()
    val courses = mutableListOf<Course>()
    val enrollments = mutableListOf<Enrollment>()

    fun generateStudentId(): String {
        val next = students.size + 1
        return "ST" + next.toString().padStart(3, '0')
    }

    fun showMenu() {
        println("\n=== Student Course Registration System ===")
        println("1. Register new student")
        println("2. Create new course")
        println("3. Enroll student in course")
        println("4. View students in a course")
        println("5. View all students")
        println("6. View all courses")
        println("7. Exit")
        print("Choose an option (1-7): ")
    }

    fun readNonEmpty(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readLine()?.trim()
            if (!input.isNullOrEmpty()) return input
            println("Input cannot be empty. Please try again.")
        }
    }

    fun registerStudent() {
        println("\n--- Register New Student ---")

        // ✅ Always ask for unique ID
        var id: String
        do {
            id = readNonEmpty("Enter student ID (e.g., ST001): ")
            if (students.any { it.id == id }) {
                println("⚠️ ID already exists. Please enter a different ID.")
                id = ""
            }
        } while (id.isEmpty())

        val name = readNonEmpty("Student name: ")

        // ✅ Email is required
        var email: String
        while (true) {
            print("Student email: ")
            val input = readLine()?.trim()
            if (!input.isNullOrEmpty()) {
                email = input
                break
            } else {
                println("⚠️ Email cannot be empty. Please enter a valid email.")
            }
        }

        val major = readNonEmpty("Major: ")

        val student = Student(id = id, name = name, email = email, major = major)
        students.add(student)

        println("✅ Student registered successfully!")
        println("ID: $id")
        println("Name: $name")
        println("Email: $email")
        println("Major: $major")
    }


    fun createCourse() {
        println("\n--- Create New Course ---")
        var courseId: String
        do {
            courseId = readNonEmpty("Course ID (e.g., CS101): ")
            if (courses.any { it.courseId == courseId }) {
                println("Course ID already exists. Please enter a different ID.")
                courseId = ""
            }
        } while (courseId.isEmpty())

        val courseName = readNonEmpty("Course name: ")

        var credits: Int
        while (true) {
            print("Credits (integer): ")
            val input = readLine()?.trim()
            try {
                val c = input?.toInt() ?: throw NumberFormatException()
                if (c <= 0) {
                    println("Credits must be positive.")
                    continue
                }
                credits = c
                break
            } catch (e: NumberFormatException) {
                println("Please enter a valid integer for credits.")
            }
        }

        val course = Course(courseId = courseId, courseName = courseName, credits = credits)
        courses.add(course)
        println("Course created: ${course.courseId} - ${course.courseName} (${course.credits} credits)")
    }

    fun enrollStudent() {
        println("\n--- Enroll Student in Course ---")
        if (students.isEmpty()) {
            println("No students registered yet.")
            return
        }
        if (courses.isEmpty()) {
            println("No courses created yet.")
            return
        }

        print("Enter student ID: ")
        val sId = readLine()?.trim() ?: ""
        val student = students.find { it.id == sId }
        if (student == null) {
            println("Student with ID '$sId' not found.")
            return
        }

        print("Enter course ID: ")
        val cId = readLine()?.trim() ?: ""
        val course = courses.find { it.courseId == cId }
        if (course == null) {
            println("Course with ID '$cId' not found.")
            return
        }

        if (enrollments.any { it.studentId == sId && it.courseId == cId }) {
            println("Student ${student.id} is already enrolled in ${course.courseId}.")
            return
        }

        enrollments.add(Enrollment(studentId = sId, courseId = cId))
        println("Successfully enrolled ${student.name} (${student.id}) in ${course.courseName} (${course.courseId}).")
    }

    fun viewCourseStudents() {
        println("\n--- View Students in a Course ---")
        if (courses.isEmpty()) {
            println("No courses available.")
            return
        }
        print("Enter course ID to view: ")
        val cId = readLine()?.trim() ?: ""
        val course = courses.find { it.courseId == cId }
        if (course == null) {
            println("Course with ID '$cId' not found.")
            return
        }

        val enrolled = enrollments.filter { it.courseId == cId }
        if (enrolled.isEmpty()) {
            println("No students enrolled in ${course.courseName} (${course.courseId}).")
            return
        }

        println("Students enrolled in ${course.courseName} (${course.courseId}):")
        enrolled.forEachIndexed { idx, enr ->
            val s = students.find { it.id == enr.studentId }
            if (s != null) {
                println("${idx + 1}. ${s.id} - ${s.name} | Major: ${s.major} | Email: ${s.email ?: "N/A"}")
            } else {
                println("${idx + 1}. [Unknown student ID: ${enr.studentId}]")
            }
        }
    }

    fun viewAllStudents() {
        println("\n--- All Registered Students ---")
        if (students.isEmpty()) {
            println("No students registered.")
            return
        }
        students.forEachIndexed { idx, s ->
            println("${idx + 1}. ${s.id} - ${s.name} | Major: ${s.major} | Email: ${s.email ?: "N/A"}")
        }
    }

    fun viewAllCourses() {
        println("\n--- All Courses ---")
        if (courses.isEmpty()) {
            println("No courses created.")
            return
        }
        courses.forEachIndexed { idx, c ->
            val enrollCount = enrollments.count { it.courseId == c.courseId }
            println("${idx + 1}. ${c.courseId} - ${c.courseName} | Credits: ${c.credits} | Enrolled: $enrollCount")
        }
    }

    while (true) {
        showMenu()
        val choiceRaw = readLine()?.trim()
        when (choiceRaw) {
            "1" -> registerStudent()
            "2" -> createCourse()
            "3" -> enrollStudent()
            "4" -> viewCourseStudents()
            "5" -> viewAllStudents()
            "6" -> viewAllCourses()
            "7" -> {
                println("Goodbye!")
                exitProcess(0)
            }
            else -> println("Invalid option. Please choose a number between 1 and 7.")
        }
    }
}

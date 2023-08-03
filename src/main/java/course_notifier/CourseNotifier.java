package course_notifier;

public class CourseNotifier {

    public static void main(String[] args) {

        final Course course = new Course("Java Multithreaded Programming");

        // two of the threads for the students waiting for notification
        //one thread for the instructor who is waiting for the course to start
        new Thread(()-> {
            synchronized (course) {
                System.out.println(Thread.currentThread().getName() + " is waiting for the course " + course.getTitle());
                try {
                    course.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " completed the course " + course.getTitle());
            }
        }, "StudentA").start();

        new Thread(()-> {
            System.out.println(Thread.currentThread().getName() + " is waiting for the course " + course.getTitle());
            synchronized (course) {
                try {
                    course.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " completed the course " + course.getTitle());
            }
        }, "StudentB").start();

        new Thread(()-> {
            synchronized (course) {
                System.out.println(Thread.currentThread().getName() + " is starting a new course " + course.getTitle());
                try {
                    Thread.sleep(4000);
                    course.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Instructor").start();
    }

} //ENDCLASS

package duke;

import duke.exceptions.EmptyCommandException;
import duke.exceptions.EmptyFindException;
import duke.exceptions.InvalidCommandException;
import duke.exceptions.MissingTimeException;
import duke.exceptions.TaskCompletionException;
import duke.exceptions.TaskDeletionException;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

import java.util.ArrayList;

/**
 * Stores the current list of tasks.
 */

public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Prints all the tasks line by line to the user.
     */
    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        int i = 1;
        for (Task t : tasks) {
            System.out.println(i + "." + t.toString());
            i++;
        }
    }

    /**
     * Adds a task to the list based on user input.
     * @param s User input
     * @throws InvalidCommandException If command is not valid.
     * @throws EmptyCommandException If task is missing description.
     * @throws MissingTimeException If task is missing time.
     */
    public void addTask(String s) throws InvalidCommandException, EmptyCommandException, MissingTimeException {
        String str = s.trim();
        if (str.equals("todo") || str.equals("deadline") || str.equals("event")) {
            throw new EmptyCommandException(str);
        }
        if (str.contains(" ")) {
            String[] arr = str.split(" ", 2);
            String str2 = arr[1];
            switch (arr[0]) {
            case "todo":
                ToDo td = new ToDo(str2);
                insert(td);
                break;
            case "deadline":
                if (str2.contains("/by")) {
                    String[] arr2 = str2.split("/by", 2);
                    if (arr2[0].isBlank()) {
                        throw new EmptyCommandException("deadline");
                    }
                    if (arr2[1].isBlank()) {
                        throw new MissingTimeException("deadline");
                    }
                    Deadline dl = new Deadline(arr2[0], arr2[1].trim());
                    insert(dl);
                } else {
                    throw new MissingTimeException("deadline");
                }
                break;
            case "event":
                if (str2.contains("/at")) {
                    String[] arr2 = str2.split("/at", 2);
                    if (arr2[0].isBlank()) {
                        throw new EmptyCommandException("event");
                    }
                    if (arr2[1].isBlank()) {
                        throw new MissingTimeException("event");
                    }
                    Event ev = new Event(arr2[0], arr2[1].trim());
                    insert(ev);
                } else {
                    throw new MissingTimeException("event");
                }
                break;
            default:
                throw new InvalidCommandException();
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    /**
     * Finds and displays tasks containing the relevant keyword
     * @param str The keyword the user is searching for.
     * @throws EmptyFindException If there is no keyword given.
     * @throws InvalidCommandException If command is not valid.
     */
    public void find(String str) throws EmptyFindException, InvalidCommandException {
        String s = str.trim();
        if (s.equals("find")) {
            throw new EmptyFindException();
        } else if (s.startsWith("find ")) {
            String keyword = s.substring(5);
            int i = 1;
            boolean flag = false;
            //search if there are any matching tasks
            for (Task t: tasks) {
                if (t.getDescription().contains(keyword)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                System.out.println("Here are the matching tasks in your list:");
                for (Task t : tasks) {
                    if (t.getDescription().contains(keyword)) {
                        System.out.println(i + "." + t);
                        i++;
                    }
                }
            } else {
                System.out.println("No matching task has been found");
            }
        } else {
            throw new InvalidCommandException();
        }
    }

    /**
     * Completes the task at the position in the list which the user specifies.
     * @param str User input
     * @throws TaskCompletionException If the number is out of range of the list.
     */
    public void completeTask(String str) throws TaskCompletionException {
        if (!str.startsWith("done ")) {
            throw new TaskCompletionException(tasks.size());
        }
        String val = str.substring(5);
        if (isInteger(val)) {
            int i = Integer.parseInt(val);
            if (i > 0 && i <= tasks.size()) {
                tasks.get(i - 1).complete();
            } else {
                throw new TaskCompletionException(tasks.size());
            }
        } else {
            throw new TaskCompletionException(tasks.size());
        }
    }

    /**
     * Deletes the task at the position in the list which the user specifies.
     * @param str User input
     * @throws TaskDeletionException If the number is out of range of the list.
     */
    public void deleteTask(String str) throws TaskDeletionException {
        if (!str.startsWith("delete ")) {
            throw new TaskDeletionException(tasks.size());
        }
        String val = str.substring(7);
        if (isInteger(val)) {
            int i = Integer.parseInt(val);
            if (i > 0 && i <= tasks.size()) {
                delete(i);
            } else {
                throw new TaskDeletionException(tasks.size());
            }
        } else {
            throw new TaskDeletionException(tasks.size());
        }
    }

    /**
     * Deletes all items in the current list.
     */
    public void clear() {
        tasks.clear();
        System.out.println("Task List has been cleared.");
    }

    private void insert(Task task) {
        tasks.add(task);
        System.out.println("Task has been added:");
        System.out.println(task.toString());
        System.out.println("You now have " + tasks.size() + " tasks in the list");
    }

    private void delete(int i) {
        Task t = tasks.get(i - 1);
        tasks.remove(i - 1);
        System.out.println("Task has been removed.");
        System.out.println(t.toString());
        System.out.println("You now have " + tasks.size() + " tasks in the list");
    }

    //helper function to check if part of user input is an integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}

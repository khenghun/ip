package duke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

/**
 * Stores the location of the saved TaskList.
 */

public class Storage {
    private String filepath;

    public Storage() {
        this.filepath = "";
    }

    /**
     * Creates a new storage with the specified directory.
     *
     * @param filepath directory to save to and load from
     */
    public Storage(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Loads the previously saved tasks into an arraylist.
     * If there is no saved directory or file, create a new folder and arraylist.
     */

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = reader.readLine()) != null) {
                Task task;
                if (str.startsWith("T")) {
                    task = ToDo.load(str);
                } else if (str.startsWith("E")) {
                    task = Event.load(str);
                } else if (str.startsWith("D")) {
                    task = Deadline.load(str);
                } else {
                    break;
                }
                tasks.add(task);
            }
            return tasks;
        } catch (FileNotFoundException e) {
            System.out.println("Starting a new task list");
            File file = new File("./data");
            file.mkdir();
            return tasks;
        } catch (IOException e) {
            e.printStackTrace();
            return tasks;
        }
    }

    /**
     * Saves the current list of tasks into the directory.
     *
     * @param tasks Current list of tasks.
     */

    public void save(TaskList tasks) {
        try {
            FileWriter writer = new FileWriter(filepath);
            for (Task t : tasks.getTasks()) {
                writer.write(t.store() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("error in saving");
        }
    }
}

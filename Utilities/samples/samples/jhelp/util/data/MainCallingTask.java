package samples.jhelp.util.data;

import jhelp.util.debug.Debug;
import jhelp.util.thread.ThreadManager;

public class MainCallingTask
{
    public static void main(String[] arguments)
    {
        //Create the simulation task
        SimulateProgressTask waitingTask = new SimulateProgressTask();
        //Link observable to tasks
        waitingTask.progress()
                   .when(value -> value == 0,
                         parameter -> Debug.verbose("Progress started"))
                   .eachTime(value -> value >= 25 && value <= 75,
                             parameter -> Debug.verbose(parameter, " inside [25, 75]"))
                   .when(value -> value == 100,
                         parameter -> Debug.verbose("Progress finished"));
        //Launch the task with 73 steps
        ThreadManager.parallel(waitingTask, 73);
    }
}

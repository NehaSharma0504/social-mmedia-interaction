package eca.learnings.socialmedia.interaction.threadDump;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadDumpAnalysis {
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println(threadInfo.toString());
        }
    }
}


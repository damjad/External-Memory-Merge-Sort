package com.bdma.dsa.cde.benchmarks;

import com.bdma.dsa.cde.utils.PropertiesHandler;

import java.io.IOException;

public class CreateDataFile
{
    public static void main(String[] args) throws IOException
    {
        PropertiesHandler p = PropertiesHandler.getInstance("/mnt/1717A37A1971CE02/WorkSpaces/BDMA/database-system-architecture/project-cmunizcu-damjad-econte/project-cmunizcu-damjad-econte-benchmarks/src/main/conf/mway-merge-sort/module-config.properties");
        MWaySortBenchmarkMain.createDataFile(1073741824, "/mnt/1717A37A1971CE02/WorkSpaces/BDMA/database-system-architecture/project-cmunizcu-damjad-econte/project-cmunizcu-damjad-econte-benchmarks/data/test-data1073741824");
    }
}

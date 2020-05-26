package com.bdma.dsa.cde;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.sorting.MultiwayMergeSortInt;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.bdma.dsa.cde.utils.Constants.*;

public class Main
{
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    private static PropertiesHandler propertiesHandler;
    private static int availableMemoryBlocks;
    private static int nParallelStreams;


    public static void main(String[] args) throws IOException
    {
        validateArgsAndInit(args);


        String outputFile = PropertiesHandler.getInstance().getProperty(M_WAY_OUTPUT_FILE);

        MultiwayMergeSortInt.sort(openDataFile(), nParallelStreams, availableMemoryBlocks, outputFile);


        LOGGER.info("Ran successfully!");
    }

    public static void validateArgsAndInit(String[] args)
    {

        boolean exitWithFailure = false;

        try
        {
            propertiesHandler = PropertiesHandler.getInstance(args.length > 0 ? args[0] : null);
            availableMemoryBlocks = args.length > 1 ? Integer.parseInt(args[1]) : PropertiesHandler.getInstance().getInteger(M_WAY_MEMORY_AVAILABLE);
            nParallelStreams = args.length > 2 ? Integer.parseInt(args[2]) : PropertiesHandler.getInstance().getInteger(M_WAY_PARLLEL_STREAMS);
        }
        catch (IOException e)
        {
            LOGGER.error("Please enter a valid path of a file or check file permissions. Error details: " + e.getMessage());
            exitWithFailure = true;
        }

        if (exitWithFailure)
        {
            System.exit(UNSUCCESSFUL_EXIT_CODE);
        }
    }

    private static InputStream<Integer> openDataFile()
    {
        InputStream<Integer> in = null;

        try
        {
            in = InputStreamFactory.createInstance();
            in.open(propertiesHandler.getProperty("data.file"));
        }
        catch (IOException e)
        {
            LOGGER.error("Could not open data file. [{}]. Please verify in properties file.", propertiesHandler.getProperty("data.file"), e);
            System.exit(UNSUCCESSFUL_EXIT_CODE);
        }

        return in;
    }
}

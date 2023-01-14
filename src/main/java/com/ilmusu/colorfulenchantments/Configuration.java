package com.ilmusu.colorfulenchantments;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Configuration
{
    // The configuration File related to this configuration
    private final String configurationFileName;
    private final File configurationFile;
    // The data that has been currently parsed from the configuration File
    private final HashMap<String, String> configuration = new HashMap<>();

    public Configuration(String mod_id, String configurationFileName)
    {
        // Gets the directory containing all the configuration files from Fabric
        Path path = Path.of(FabricLoader.getInstance().getConfigDir()+"/"+mod_id);
        // Creates the configuration File in memory
        this.configurationFileName = configurationFileName+".properties";
        this.configurationFile = path.resolve(this.configurationFileName).toFile();
        // Creates the configuration file on disk (only if not already existing)
        this.createConfigurationFile();
        // Loads the current file configuration on the memory
        this.reloadConfigurationFromFile();
    }

    private void createConfigurationFile()
    {
        if(this.configurationFile.exists())
            return;

        try
        {
            this.configurationFile.getParentFile().mkdirs();
            this.configurationFile.createNewFile();
        }
        catch (IOException exception)
        {
            Resources.LOGGER.error("Could not create configuration file ("+configurationFileName+") !");
            Resources.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    private void writeConfigurationFile()
    {
        if(!this.configurationFile.exists())
            return;

        try
        {
            FileWriter writer = new FileWriter(this.configurationFile);
            // Resetting the file to an empty file
            writer.write("");
            // Setting all the current configurations
            for(String key : this.configuration.keySet())
                writer.append(key).append("=").append(this.configuration.get(key)).append("\n");
            // Closing the file because there is noting more to do
            writer.close();
        }
        catch (IOException exception)
        {
            Resources.LOGGER.error("Could not write configuration file ("+configurationFileName+") !");
            Resources.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    private void reloadConfigurationFromFile()
    {
        Scanner reader;
        try
        {
            reader = new Scanner(this.configurationFile);
        }
        catch (FileNotFoundException exception)
        {
            Resources.LOGGER.error("Could not load configuration file ("+configurationFileName+") !");
            Resources.LOGGER.error(exception.getLocalizedMessage());
            return;
        }

        for(int line = 1; reader.hasNextLine(); line ++)
            parseConfigurationEntry(reader.nextLine(), line);
    }

    private void parseConfigurationEntry(String entry, int line)
    {
        // The entry is not considered if it is an empty line or a comment
        if(entry.isEmpty() || entry.startsWith("#"))
            return;

        // The configuration is composed of two parts separated by "="
        String[] parts = entry.split("=", 2);
        // So, if there are not two, parts, it is a syntax error
        if(parts.length != 2)
        {
            Resources.LOGGER.error("Syntax error in configuration file ("+configurationFileName+") at line "+line+"!");
            return;
        }

        this.configuration.put(parts[0].trim(), parts[1].trim());
    }

    public void setConfiguration(String name, String value)
    {
        // The name and the value are trimmed just to be sure
        name = name.trim();
        value = value.trim();
        // Do not reset it if already has the same value
        if(Objects.equals(this.configuration.get(name), value))
            return;

        this.configuration.put(name, value);
        this.writeConfigurationFile();
    }

    public void setNonExistentConfiguration(String name, String value)
    {
        // The name and the value are trimmed just to be sure
        name = name.trim();
        value = value.trim();
        // If the name already exists, do not reset it
        if(this.configuration.containsKey(name))
            return;

        this.setConfiguration(name, value);
    }

    public String getConfiguration(String name)
    {
        return this.configuration.get(name.trim());
    }
}
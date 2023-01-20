package com.genfare.gds.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.genfare.gds.optionsImpl.DeviceAuthOptImpl;
import com.genfare.gds.optionsImpl.GdsFilesImpl;
import com.genfare.gds.optionsImpl.SetOptionsImpl;


public class BasicOptions {
	
	public static ArrayList<String> commands = new ArrayList<String>();
	
	public static void main(String[] args) {

		BasicOptions basicOptions = new BasicOptions();
		Options options = new Options();
		
		options.addOption("authenticate", false, "authenticate farebox");
		
		OptionBuilder.withArgName("env tenant environment");
		OptionBuilder.hasArgs(3);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("set environmet variables");
		Option opt = OptionBuilder.create("set");
		options.addOption(opt);
		
		OptionBuilder.withArgName("list");
		OptionBuilder.hasArgs(1);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("list files from the S3");
		Option opt1 = OptionBuilder.create("gds");
		options.addOption(opt1);
		
		OptionBuilder.withArgName("get file filename");
		OptionBuilder.hasArgs(3);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("get file from S3");
		Option opt2 = OptionBuilder.create("gds");
		options.addOption(opt2);
		
		OptionBuilder.withArgName("get autoload filename");
		OptionBuilder.hasArgs(3);
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription("get autoload file from S3");
		Option opt3 = OptionBuilder.create("gds");
		options.addOption(opt3);
		
		System.out.println("Usage : <command> <option> <arguments..>");
		for (;;) {
			try {
				System.out.println();
				if (args.length == 0)
					System.out.printf("GDS >");
				else {
					System.out.printf(args[0] + ">");
				}

				BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

				String args2 = null;
				try {
					args2 = inp.readLine();
					commands.add(args2);
					args2 = "-" + args2;
				} catch (IOException e) {

					e.printStackTrace();
					continue;
				}
				String[] args3 = args2.split(" ");
				CommandLine line = new BasicParser().parse(options, args3);
				String command = args3[0].toLowerCase();
				if (line.hasOption(command)) {
					basicOptions.getResponse(command, line);
				} else {
					System.out.println("Command not found");
				}

			} catch (Exception ex) {
				System.out.println("Error:" + ex.getMessage());
				continue;
			}
		}

	}
	public void getResponse(String option, CommandLine line) {
	
		DeviceAuthOptImpl deviceAuth;
		switch (option) {
			case "-set":
				SetOptionsImpl setOptions = new SetOptionsImpl();
				setOptions.settingEnv(line);
				break;
			case "-authenticate":
				deviceAuth = new DeviceAuthOptImpl();
				System.out.println(deviceAuth.authenticate());
				break;
			case "-gds":
				GdsFilesImpl gdsOptions = new GdsFilesImpl();
				gdsOptions.getGdsFiles(line);
				break;
			case "-exit":
				System.exit(0);
		}
	}
}

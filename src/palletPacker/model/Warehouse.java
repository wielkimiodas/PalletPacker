package palletPacker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import palletPacker.processor.Configuration;

public class Warehouse {

	private Package[] packages;
	private Pallet[] pallets;

	public Warehouse() {

	}

	public Configuration getDefaultConfiguration() {
		return new Configuration(packages);
	}

	public Package[] getPackages() {
		return packages;
	}

	private Pallet getPalletByName(String name) {
		for (int i = 0; i < pallets.length; i++) {
			if (pallets[i].getName().equals(name)) {
				return pallets[i];
			}
		}
		return null;
	}

	public Pallet[] getPallets() {
		return pallets;
	}

	public void ReadData(String dataFilePath) throws NumberFormatException,
			IOException {
		final File dataFile = new File(dataFilePath);
		final BufferedReader br = new BufferedReader(new FileReader(dataFile));
		final int palletsAmount = Integer.parseInt(br.readLine());

		pallets = new Pallet[palletsAmount];

		for (int i = 0; i < palletsAmount; i++) {
			final String line = br.readLine();
			final String[] lineSplitted = line.split("\t");
			// id is always from 1 to n incrementally, no need to parse it
			final int id = i;
			// pallet name
			final String name = lineSplitted[0];
			// pallet area
			final int area = (int) (Float.parseFloat(lineSplitted[1]) * 10);
			// pallet extension height
			final int h = (int) (Float.parseFloat(lineSplitted[2]) * 10);
			// pallet max height
			final int hMax = (int) (Float.parseFloat(lineSplitted[3]) * 10);
			// pallet max load
			final int loadMax = (int) (Float.parseFloat(lineSplitted[3]) * 10);
			// new pallet object
			final Pallet p = new Pallet(id, name, area, h, hMax, loadMax);
			pallets[i] = p;
		}

		final int packagesAmount = Integer.parseInt(br.readLine());
		packages = new Package[packagesAmount];
		for (int i = 0; i < packagesAmount; i++) {
			final String line = br.readLine();
			final String[] lineSplitted = line.split("\t");
			// package id
			final String id = lineSplitted[0];
			// package volume
			final int v = (int) (Float.parseFloat(lineSplitted[1]) * 10);
			// package load
			final int load = (int) (Float.parseFloat(lineSplitted[2]) * 10);
			// default pallet name for current package
			final String defaultPalletName = lineSplitted[3];
			// default pallet object for current package
			final Pallet defPallet = getPalletByName(defaultPalletName);
			// new package object
			final Package p = new Package(id, v, defPallet, load);
			packages[i] = p;
		}
		br.close();
	}
}

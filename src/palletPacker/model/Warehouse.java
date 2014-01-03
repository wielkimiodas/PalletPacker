package palletPacker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import palletPacker.processor.Configuration;

public class Warehouse {

	private Package[] packages;
	private Pallet[] pallets;

	public Package[] getPackages() {
		return packages;
	}

	public Pallet[] getPallets() {
		return pallets;
	}

	public Warehouse() {

	}

	private Pallet getPalletByName(String name) {
		for (int i = 0; i < pallets.length; i++) {
			if (pallets[i].name.equals(name)) {
				return pallets[i];
			}
		}
		return null;
	}

	public void ReadData(String dataFilePath) throws NumberFormatException,
			IOException {
		File dataFile = new File(dataFilePath);
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		int palletsAmount = Integer.parseInt(br.readLine());

		pallets = new Pallet[palletsAmount];

		for (int i = 0; i < palletsAmount; i++) {
			String line = br.readLine();
			String[] lineSplitted = line.split("\t");
			int id = i;
			int v = Integer.parseInt(lineSplitted[1]);
			float h = Float.parseFloat(lineSplitted[2]);
			float hMax = Float.parseFloat(lineSplitted[3]);
			Pallet p = new Pallet(id, lineSplitted[0], v, h, hMax);
			pallets[i] = p;
		}

		int packagesAmount = Integer.parseInt(br.readLine());
		packages = new Package[packagesAmount];
		for (int i = 0; i < packagesAmount; i++) {
			String line = br.readLine();
			String[] lineSplitted = line.split("\t");
			int v = Integer.parseInt(lineSplitted[1]);
			String defaultPalletName = lineSplitted[2];
			Pallet defPallet = getPalletByName(defaultPalletName);
			Set<Pallet> compPallets = new HashSet<>();
			if (lineSplitted.length > 3) {
				for (int j = 0; j < lineSplitted.length - 3; j++) {
					compPallets.add(getPalletByName(lineSplitted[j + 3]));
				}

			}
			Package p = new Package(lineSplitted[0], v, defPallet, compPallets);
			packages[i] = p;
		}
		br.close();
	}

	public Configuration getDefaultConfiguration(){
		return new Configuration(packages, new boolean[packages.length]);
	}
}

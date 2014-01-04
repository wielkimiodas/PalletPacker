package palletPacker.TestInstanceCreator;

import java.io.BufferedWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import palletPacker.model.Package;
import palletPacker.model.Pallet;

public class Randomizer {
	private final static int MIN_AREA = 50;
	private final static int MAX_AREA = 1200;
	private final static int MIN_EXT_H = 50;
	private final static int MAX_EXT_H = 1200;
	private final static int MIN_EXT_COUNT = 50;
	private final static int MAX_EXT_COUNT = 1200;
	
	private Pallet pallets[];
	private Package pckgs[];

	public void GenerateRandomInstanceFile(int palletCount, int pckgsCount,
			String filename) {
		RandomPallets(palletCount);
		RandomPackages(pckgsCount);
		try {
			PrintResults(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Saved to /data/" + filename);
	}

	private int getUnfairRandom(int maxValue, float unfairness){
		return (int)(Math.pow(Math.random(), unfairness) * maxValue); 
	}
	
	private int getRandom(int minValue, int maxValue){
		return (int)(Math.random() * (maxValue - minValue + 1)) + minValue;
	}

	private void RandomPallets(int howManyPalletTypes) {
		pallets = new Pallet[howManyPalletTypes];

		for (int i = 0; i < howManyPalletTypes; i++) {
			String name = "Pallet" + (i + 1);

			// int random <50,1200>
			int area = getRandom(MIN_AREA, MAX_AREA);
			// float random <1,5>
			float extHeight = getRandom(10 * MIN_EXT_H, 10 * MAX_EXT_H) / 10f;

			// int random <1,10>
			int extCount = getRandom(MIN_EXT_COUNT, 2 * MAX_EXT_COUNT);

			Pallet p = new Pallet(i, name, area, extHeight, extHeight
					* extCount);
			pallets[i] = p;
		}
	}
	
	private void RandomPackages(int howManyPackages) {
		pckgs = new Package[howManyPackages];
		for (int i = 0; i < howManyPackages; i++) {
			String id = "p" + (i + 1);
			int compatiblePalletsCount = getUnfairRandom(pallets.length, 5) + 1;
			Pallet[] compatiblePallets = new Pallet[compatiblePalletsCount];

			for (int j = 0; j < compatiblePalletsCount; j++) {
				ArrayList<Pallet> availablePalletTypes = new ArrayList<Pallet>(
						Arrays.asList(pallets));
				int elementId = getRandom(0, availablePalletTypes.size() - 1);
				compatiblePallets[j] = availablePalletTypes.get(elementId);
				availablePalletTypes.remove(elementId);
			}

			int maxVolumeInAvailablePallets = Integer.MIN_VALUE;
			for (int j = 0; j < compatiblePallets.length; j++) {
				if (compatiblePallets[j].getMaxVolume() > maxVolumeInAvailablePallets)
					maxVolumeInAvailablePallets = (int) compatiblePallets[j]
							.getMaxVolume();
			}

			int volume = getUnfairRandom(maxVolumeInAvailablePallets, 20) + 1;
			Set<Pallet> compPalletSet = new HashSet<Pallet>();
			for (int j = 1; j < compatiblePallets.length; j++) {
				compPalletSet.add(compatiblePallets[j]);
			}
			Package p = new Package(id, volume, compatiblePallets[0],
					compPalletSet);

			pckgs[i] = p;
		}
	}

	private void PrintResults(String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("data/" + fileName)));

		writer.write("" + pallets.length);
		writer.newLine();
		for (int i = 0; i < pallets.length; i++) {
			Pallet curr = pallets[i];
			writer.write(curr.getName() + "\t" + curr.getArea() + "\t"
					+ curr.getExtensionHeight() + "\t" + curr.getMaxHeight());
			writer.newLine();
		}
		writer.write("" + pckgs.length);
		writer.newLine();
		for (int i = 0; i < pckgs.length; i++) {
			Package curr = pckgs[i];
			writer.write(curr.getId() + "\t" + curr.getVolume() + "\t"
					+ curr.getDefaultPallet().getName());
			if (curr.getCompatiblePallets() != null)
				for (Pallet p : curr.getCompatiblePallets()) {
					writer.write("\t" + p.getName());
				}
			writer.newLine();
		}

		writer.flush();
		writer.close();
	}

}

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

	private void RandomPallets(int howManyPalletTypes) {
		pallets = new Pallet[howManyPalletTypes];
		Random rand = new Random();

		for (int i = 0; i < howManyPalletTypes; i++) {
			String name = "Pallet" + i;

			// int random <50,1200)
			int area = rand.nextInt(1150) + 50;
			// float random <1,5);
			float extHeight = (rand.nextInt(40) + 10) / 10f;

			// int random <1,10>
			int extCount = rand.nextInt(10) + 1;

			Pallet p = new Pallet(i, name, area, extHeight, extHeight
					* extCount);
			pallets[i] = p;
		}
	}

	private void RandomPackages(int howManyPackages) {
		pckgs = new Package[howManyPackages];
		Random rand = new Random();
		for (int i = 0; i < howManyPackages; i++) {
			String id = "p" + i;
			int compatiblePalletsCount = rand.nextInt(pallets.length) + 1;
			Pallet[] compatiblePallets = new Pallet[compatiblePalletsCount];

			for (int j = 0; j < compatiblePalletsCount; j++) {
				ArrayList<Pallet> availablePalletTypes = new ArrayList<Pallet>(
						Arrays.asList(pallets));
				int elementId = rand.nextInt(availablePalletTypes.size());
				compatiblePallets[j] = availablePalletTypes.get(elementId);
				availablePalletTypes.remove(elementId);
			}

			int maxVolumeInAvailablePallets = Integer.MIN_VALUE;
			for (int j = 0; j < compatiblePallets.length; j++) {
				if (compatiblePallets[j].getMaxVolume() > maxVolumeInAvailablePallets)
					maxVolumeInAvailablePallets = (int) compatiblePallets[j]
							.getMaxVolume();
			}

			int volume = rand.nextInt(maxVolumeInAvailablePallets) + 1;
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

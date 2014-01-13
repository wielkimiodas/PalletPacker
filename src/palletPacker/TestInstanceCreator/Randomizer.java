//package palletPacker.TestInstanceCreator;
//
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//import palletPacker.model.Package;
//import palletPacker.model.Pallet;
//import sun.security.x509.AVA;
//
//public class Randomizer {
//	private final static int MIN_AREA = 50;
//	private final static int MAX_AREA = 1200;
//	private final static int MIN_EXT_H = 50;
//	private final static int MAX_EXT_H = 1200;
//	private final static int MIN_EXT_COUNT = 50;
//	private final static int MAX_EXT_COUNT = 1200;
//
//	private Pallet pallets[];
//	private Package pckgs[];
//
//	public void GenerateRandomInstanceFile(int palletCount, int pckgsCount,
//			String filename) {
//		RandomPallets(palletCount);
//		RandomPackages(pckgsCount);
//		try {
//			PrintResults(filename);
//		} catch (final IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Saved to /data/" + filename);
//	}
//
//	private int getRandom(int minValue, int maxValue) {
//		return (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
//	}
//
//	private int getUnfairRandom(int maxValue, float unfairness) {
//		return (int) (Math.pow(Math.random(), unfairness) * maxValue);
//	}
//
//	private void PrintResults(String fileName) throws IOException {
//		final BufferedWriter writer = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream("data/" + fileName)));
//
//		writer.write("" + pallets.length);
//		writer.newLine();
//		for (int i = 0; i < pallets.length; i++) {
//			final Pallet curr = pallets[i];
//			writer.write(curr.getName() + "\t" + curr.getArea() + "\t"
//					+ curr.getExtensionHeight() + "\t" + curr.getMaxHeight());
//			writer.newLine();
//		}
//		writer.write("" + pckgs.length);
//		writer.newLine();
//		for (int i = 0; i < pckgs.length; i++) {
//			final Package curr = pckgs[i];
//			writer.write(curr.getId() + "\t" + curr.getVolume() + "\t"
//					+ curr.getDefaultPallet().getName());			
//			writer.newLine();
//		}
//
//		writer.flush();
//		writer.close();
//	}
//
//	private void RandomPackages(int howManyPackages) {
//		pckgs = new Package[howManyPackages];
//		for (int i = 0; i < howManyPackages; i++) {
//			final String id = "p" + (i + 1);
//			//final int compatiblePalletsCount = getUnfairRandom(pallets.length, 5) + 1;
//			
//			final ArrayList<Pallet> availablePalletTypes = new ArrayList<Pallet>(Arrays.asList(pallets));
//			
//			int defaultPalletId = getUnfairRandom(pallets.length, 5);
//			final Pallet defaultPallet = availablePalletTypes.get(defaultPalletId);			
//			
//			//final int volume = getUnfairRandom(maxVolumeInAvailablePallets, 20) + 1;
//			final Set<Pallet> compPalletSet = new HashSet<Pallet>();
////			for (int j = 1; j < compatiblePallets.length; j++) {
////				compPalletSet.add(compatiblePallets[j]);
////			}
//			//final Package p = new Package(id, volume,  compPalletSet);
//
//			//pckgs[i] = p;
//		}
//	}
//
//	private void RandomPallets(int howManyPalletTypes) {
//		pallets = new Pallet[howManyPalletTypes];
//
//		for (int i = 0; i < howManyPalletTypes; i++) {
//			final String name = "Pallet" + (i + 1);
//
//			// int random <50,1200>
//			final int area = getRandom(MIN_AREA, MAX_AREA);
//			// float random <1,5>
//			final float extHeight = getRandom(10 * MIN_EXT_H, 10 * MAX_EXT_H) / 10f;
//
//			// int random <1,10>
//			final int extCount = getRandom(MIN_EXT_COUNT, 2 * MAX_EXT_COUNT);
//
//			final Pallet p = new Pallet(i, name, area, extHeight, extHeight
//					* extCount);
//			pallets[i] = p;
//		}
//	}
//
// }

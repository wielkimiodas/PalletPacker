package palletPacker.TestInstanceCreator;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Randomizer r = new Randomizer();
		int palletsCount = 100;
		int pckgsCount = 1000;
		String fileName = "bigTest2.txt";
		r.GenerateRandomInstanceFile(palletsCount, pckgsCount, fileName);
	}

}

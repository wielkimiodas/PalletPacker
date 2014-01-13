package palletPacker.processor;

public class Result implements Comparable<Result> {
	private final int totalArea;
	private final float minPalletVolume;

	public Result(int totalArea, float minPalletVolume) {
		this.totalArea = totalArea;
		this.minPalletVolume = minPalletVolume;
	}

	@Override
	public int compareTo(Result otherResult) {
		if (totalArea == otherResult.totalArea) {
			return (int) (otherResult.minPalletVolume - minPalletVolume);
		} else {
			return otherResult.totalArea - totalArea;
		}
	}

	public float getMinPalletVolume() {
		return minPalletVolume;
	}

	public int getTotalArea() {
		return totalArea;
	}
}

package put.two.to.contest.model;

import java.util.List;

public class Result implements Comparable<Result> {
	float area;
	float volume;
	List<Package> packages;
	public int count = 0;

	public Result(float area, float volume, List<Package> packages, int count) {
		super();
		this.area = area;
		this.volume = volume;
		this.packages = packages;
		this.count = count;
	}

	public float getArea() {
		return area;
	}

	public float getVolume() {
		return volume;
	}

	public List<Package> getPackages() {
		return packages;
	}

	@Override
	public int compareTo(Result o) {
		if (area != o.area) {
			return area < o.area ? 1 : -1;
		}

		float diff = volume - o.volume;
		if (diff == 0) {
			return 0;
		}

		return diff < 0 ? 1 : -1;
	}

	public static Result getWorst() {
		return new Result(Float.MAX_VALUE, Float.MAX_VALUE, null, 0);
	}
}

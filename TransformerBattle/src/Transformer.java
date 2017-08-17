import java.util.ArrayList;
import java.util.Collections;

public class Transformer implements Comparable<Transformer> {

	private String name;
	private String robotType;
	private int strength;
	private int intelligence;
	private int speed;
	private int endurance;
	private int rank;
	private int courage;
	private int firePower;
	private int skill;

	private ArrayList<Transformer> decipticonArray;
	private ArrayList<Transformer> autobotArray;
	private ArrayList<Transformer> losingTeamMemRemaining; // remaining team
															// members

	private int autobotDestroyedCount;
	private int decipticonDestroyedCount;

	private String kingOfAutobot = "Optimus Prime";
	private String kingofDecipticon = "Predaking";

	public Transformer(String name, String robotType, int strength, int intelligence, int speed, int endurance,
			int rank, int courage, int firePower, int skill) {

		this.name = name;
		this.robotType = robotType;
		this.strength = strength;
		this.intelligence = intelligence;
		this.speed = speed;
		this.endurance = endurance;
		this.rank = rank;
		this.courage = courage;
		this.firePower = firePower;
		this.skill = skill;
		this.decipticonArray = new ArrayList<Transformer>();
		this.autobotArray = new ArrayList<Transformer>();
		this.losingTeamMemRemaining = new ArrayList<Transformer>();

	}

	public void init(Transformer[] transformers) {

		// categorizing transformers into Autobots or Decipticon for battle
		// later
		for (Transformer t : transformers) {

			if (t.getRobotType() == "A") {

				autobotArray.add(t);

			} else {

				decipticonArray.add(t);
			}

		}

		// call sort to sort based on rank
		sortBasedOnRank();

		// after sorting start battle
		battle();
	}

	public void battle() {

		String winingteam = null;
		String losingteam = null;
		int arraySize;
		ArrayList<Transformer> autobotCloneArray = autobotArray;
		ArrayList<Transformer> decipticonCloneArray = decipticonArray;
		boolean ultimate = false;

		// check array size, we are using the lesser one
		if (autobotArray.size() > decipticonArray.size()) {
			arraySize = decipticonArray.size();
		} else {
			arraySize = autobotArray.size();
		}

		for (int i = 0; i < arraySize; i++) {

			// special rules
			if (autobotArray.get(i).getName().equals(kingOfAutobot)
					&& decipticonArray.get(i).getName().equals(kingofDecipticon)) {
				// destroy all competitors with no one left
				if (autobotArray.size() == decipticonArray.size()) {
					// tie
					winingteam = "T";
					losingTeamMemRemaining = null;
					ultimate = true;
					break;
				} else if (autobotArray.size() > decipticonArray.size()) {
					winingteam = "A";
					losingteam = "B";
					ultimate = true;
					break;
				} else {
					winingteam = "B";
					losingteam = "A";
					ultimate = true;
					break;
				}

			} else if (autobotArray.get(i).getName().equals(kingOfAutobot)) {
				// decipticon gets destroyed and battle with optimus prime
				decipticonDestroyedCount++;
				decipticonCloneArray.remove(i);
				break;
			} else if (decipticonArray.get(i).getName().equals(kingofDecipticon)) {
				// autobot gets destroyed and battle with predaking
				autobotDestroyedCount++;
				autobotCloneArray.remove(i);
				break;
			}

			// first case: down 4 or more points of courage and 3 or more points
			// of strength.
			if (checkCourageAndStrength(autobotArray.get(i), decipticonArray.get(i)) != null) {

				if (checkCourageAndStrength(autobotArray.get(i), decipticonArray.get(i)).equals(autobotArray.get(i))) {

					System.out.println("battle first case returns a, b is destroyed");
					decipticonDestroyedCount++;
					// remove from array
					decipticonCloneArray.remove(i);
					break;

				}
				if (checkCourageAndStrength(autobotArray.get(i), decipticonArray.get(i))
						.equals(decipticonArray.get(i))) {

					System.out.println("battle first case returns b, a is destroyed");
					autobotDestroyedCount++;
					// remove from array
					autobotCloneArray.remove(i);
					break;

				}

			}
			// second case: 3 or more points of skill above their opponent.
			if (checkSkill(autobotArray.get(i), decipticonArray.get(i)) != null) {
				if (checkSkill(autobotArray.get(i), decipticonArray.get(i)).equals(autobotArray.get(i))) {

					System.out.println("battle second case returns a, b is destroyed");
					decipticonDestroyedCount++;
					// remove from array
					decipticonCloneArray.remove(i);
					break;

				}

				if (checkSkill(autobotArray.get(i), decipticonArray.get(i)).equals(decipticonArray.get(i))) {

					System.out.println("battle second case returns b, a is destroyed");
					autobotDestroyedCount++;
					// remove from array
					autobotCloneArray.remove(i);
					break;

				}
			}

			// third case: The winner is the Transformer with the highest
			// overall rating.

			if (checkOverallRating(autobotArray.get(i), decipticonArray.get(i)) == null) {

				System.out.println("battle third case returns null, a and b are destroyed");
				autobotCloneArray.remove(i);
				decipticonCloneArray.remove(i);

			} else if (checkOverallRating(autobotArray.get(i), decipticonArray.get(i)).equals(autobotArray.get(i))) {

				System.out.println("battle third case returns a, b is destroyed");
				decipticonDestroyedCount++;
				// remove from array
				decipticonCloneArray.remove(i);
				break;

			} else {
				System.out.println("battle third case returns b, a is destroyed");
				autobotDestroyedCount++;
				// remove from array
				autobotCloneArray.remove(i);
				break;
			}

		}

		if (ultimate == false) {
			if (autobotDestroyedCount != decipticonDestroyedCount) {
				if (autobotDestroyedCount > decipticonDestroyedCount) {
					// decipticon wins
					winingteam = "B";
					losingteam = "A";
					losingTeamMemRemaining = autobotCloneArray;

				} else {
					// autobot wins
					winingteam = "A";
					losingteam = "B";
					losingTeamMemRemaining = decipticonCloneArray;
				}
			} else {
				winingteam = "T";
				losingTeamMemRemaining = null;
			}
		}

		output(arraySize, winingteam, losingteam, losingTeamMemRemaining);

	}

	// output function for printing
	public static void output(int arraysize, String winingteam, String losingteam, ArrayList<Transformer> remaining) {

		if (winingteam.equals("A")) {
			winingteam = "Autobots";
			losingteam = "Decipticon";
		} else if (winingteam.equals("B")) {
			winingteam = "Decipticon";
			losingteam = "Autobots";
		} else {
			winingteam = "Tie";
			losingteam = "Tie";
		}

		// battles played
		System.out.println(arraysize + " battle(s)");
		// wining team
		System.out.println("Wining Team " + "(" + winingteam + ")");
		// Survivors from the losing team
		StringBuilder str = new StringBuilder("Survivors from the losing team " + "(" + losingteam + "): ");

		if (remaining != null) {

			for (int k = 0; k < remaining.size(); k++) {
				str.append(remaining.get(k).getName());
				str.append(" ");
			}
			System.out.println(str);

		} else {
			str.append(" It was a tie");
			System.out.println(str);
		}

	}

	// check courage and strength
	public Transformer checkCourageAndStrength(Transformer a, Transformer b) {

		System.out.println("checkCourageAndStrength called");

		if ((a.getCourage() - b.getCourage() >= 4) && (a.getStrength() - b.getStrength() >= 3)) {

			System.out.println("checkCourageAndStrength return a");
			return a;

		} else if ((b.getCourage() - a.getCourage() >= 4) && (b.getStrength() - a.getStrength() >= 3)) {

			System.out.println("checkCourageAndStrength return b");
			return b;

		} else {

			System.out.println("checkCourageAndStrength return null");
			return null;

		}

	}

	// check skill
	public Transformer checkSkill(Transformer a, Transformer b) {

		if ((a.getSkill() - b.getSkill() >= 3)) {

			System.out.println("checkSkill return a");
			return a;

		} else if ((b.getSkill() - a.getSkill() >= 3)) {

			System.out.println("checkSkill return b");
			return b;

		} else {

			System.out.println("checkSkill return null");
			return null;

		}
	}

	// check overall rating
	public Transformer checkOverallRating(Transformer a, Transformer b) {

		int aOverall, bOverall;
		aOverall = a.getOverallRating(a.strength, a.intelligence, a.speed, a.endurance, a.firePower);
		bOverall = b.getOverallRating(b.strength, b.intelligence, b.speed, b.endurance, b.firePower);

		if (aOverall == bOverall) {
			// Both Destroyed
			return null;

		} else if (aOverall > bOverall) {
			System.out.println("checkSkill return a");
			return a;

		} else {
			System.out.println("checkSkill return b");
			return b;

		}
	}

	@Override
	public int compareTo(Transformer o) {
		return o.rank - rank;
	}

	// sorts the transformers based on ranks
	public void sortBasedOnRank() {
		Collections.sort(autobotArray);
		Collections.sort(decipticonArray);
	}

	//////////////////////////////////////// setters and getters
	//////////////////////////////////////// ////////////////////////////////////////////////////
	public int getSkill() {
		return skill;
	}

	public int getStrength() {
		return strength;
	}

	public int getCourage() {
		return courage;
	}

	public String getName() {
		return name;
	}

	public String getRobotType() {
		return robotType;
	}

	private int getOverallRating(int st, int i, int s, int e, int f) {
		return st + i + s + e + f;
	}

	///////////////////////////////////////////////////////////////////////// main
	///////////////////////////////////////////////////////////////////////// function
	///////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////////////////////////////

	public static void main(String args[]) {

		Transformer t = new Transformer("", "", 0, 0, 0, 0, 0, 0, 0, 0);

		// testing second case
		Transformer t1 = new Transformer("Soundwave", "D", 8, 9, 2, 6, 7, 5, 6, 10);
		Transformer t2 = new Transformer("Bluestreak", "A", 6, 6, 7, 9, 5, 2, 9, 7);
		Transformer t3 = new Transformer("Bee", "A", 4, 4, 4, 4, 4, 4, 4, 4);

		// testing first case
		Transformer t4 = new Transformer("Snorlax:", "D", 7, 4, 4, 4, 4, 4, 4, 4);
		Transformer t5 = new Transformer("Evve", "A", 4, 4, 4, 4, 4, 0, 4, 4);

		// testing third case
		Transformer t6 = new Transformer("mew", "D", 4, 4, 4, 4, 4, 4, 4, 4);
		Transformer t7 = new Transformer("mewto", "A", 4, 4, 4, 4, 4, 0, 4, 4);
		
		// testing special case optimus prime
		Transformer t8 = new Transformer("mew", "D", 4, 4, 4, 4, 4, 4, 4, 4);
		Transformer t9 = new Transformer("Optimus Prime", "A", 4, 4, 4, 4, 4, 4, 4, 4);
		
		// testing special case predaking
		Transformer t10 = new Transformer("Predaking", "D", 4, 4, 4, 4, 4, 4, 4, 4);
		Transformer t11 = new Transformer("mewto", "A", 4, 4, 4, 4, 4, 4, 4, 4);
		
		// testing special case optimus prime and predaking
		Transformer t12 = new Transformer("Predaking", "D", 4, 4, 4, 4, 4, 4, 4, 4);
		Transformer t13 = new Transformer("Optimus Prime", "A", 4, 4, 4, 4, 4, 4, 4, 4);

		Transformer[] tArray = { t12, t13 };
		t.init(tArray);

	}

}

package org.david;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AntPassingPole {

	private static enum Direction {

		Right, Left;
		static Direction valueOf(int i) {
			if (i == 0)
				return Left;
			else
				return Right;

		}
	}

	private static enum Status {
		OutOfRange, Working;
	}

	private static class Ant {
		private int id;
		private int x;
		private Direction direction;
		private Status status;
		private Pole pole;

		Ant(int id, int x, Direction direction, Pole pole) {
			this.id = id;
			this.x = x;
			this.direction = direction;
			this.status = Status.Working;
			this.pole = pole;
		}

		int getId() {
			return this.id;
		}

		int getX() {
			return this.x;
		}

		Direction getDirection() {
			return this.direction;
		}

		void changeDirection() {
			if (this.direction.equals(Direction.Right))
				this.direction = Direction.Left;
			if (this.direction.equals(Direction.Left))
				this.direction = Direction.Right;
		}

		Status getStatus() {
			return this.status;
		}

		void setOutOfRange() {
			this.status = Status.OutOfRange;
		}

		void run() {
			if (this.status.equals(Status.OutOfRange))
				return;
			if (direction.equals(Direction.Right))
				x += 1;
			if (direction.equals(Direction.Left))
				x -= 1;
			pole.updateX(this);
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof Ant))
				return false;
			Ant other = (Ant) obj;
			return other.id == this.id;
		}

		public int hashCode() {
			return id;
		}

	}

	private static class Pole {

		private int start = 0;
		private int end = 27;
		private Set[] poleElements = new HashSet[28];
		private Map<Integer, Integer> idAntMap = new HashMap<Integer, Integer>(
				5);

		Pole() {
			for (int i = 0; i < poleElements.length; i++) {
				poleElements[i] = new HashSet();
			}
		}

		void placeAnts(Set<Ant> ants) {
			for (Ant ant : ants) {
				int x = ant.getX();
				poleElements[x].add(ant);
				idAntMap.put(ant.getId(), ant.getX());
			}
		}

		boolean isEmpty() {
			return idAntMap.size() == 0;
		}

		void updateX(Ant ant) {
			if (ant.getX() < start || ant.getX() > end) {
				ant.setOutOfRange();
				idAntMap.remove(ant.getId());
				return;
			}
			int prevX = idAntMap.get(ant.getId());
			poleElements[prevX].remove(ant);
			poleElements[ant.getX()].add(ant);
			Set curentElement = poleElements[ant.getX()];

			if (curentElement.size() == 2) {
				Object[] antsInSamePostion = curentElement.toArray();
				Ant first = (Ant) antsInSamePostion[0];
				Ant second = (Ant) antsInSamePostion[1];
				boolean condition1 = (first.getId() < second.getId())
						&& first.direction.equals(Direction.Right)
						&& second.direction.equals(Direction.Left);
				boolean condition2 = (first.getId() > second.getId())
						&& first.direction.equals(Direction.Left)
						&& second.direction.equals(Direction.Right);
				if (condition1 || condition2) {
					first.changeDirection();
					second.changeDirection();
				}
			}

		}

	}

	public static int getTotalCaseNum(int base, int n) {
		int totalCaseNum = 1;
		for (int i = 0; i < n; i++) {
			totalCaseNum = totalCaseNum * base;
		}
		return totalCaseNum;
	}

	public static void main(String[] arg) {

		int[] xs = { 3, 7, 11, 17, 23 };
		int totalCaseNum = getTotalCaseNum(Direction.values().length, xs.length);
		int maxTime=0;

		for (int i = 0; i < totalCaseNum; i++) {
			Set<Ant> ants = new HashSet<Ant>();
			Pole pole = new Pole();
			int remaining = i;
			for (int j = 0; j < xs.length; j++) {
				int mod = remaining % Direction.values().length;
				remaining=remaining / Direction.values().length;
				System.out.print(remaining+" ");
				ants.add(new Ant(j, xs[j], Direction.valueOf(mod), pole));
				System.out.print(Direction.valueOf(mod)+" ");
			}
			pole.placeAnts(ants);
			int time = 0;
			while (!pole.isEmpty()) {
				time++;
				for (Ant ant : ants) {
					Status status = ant.getStatus();
					if (status.equals(Status.OutOfRange))
						continue;
					ant.run();
				}
			}
			if(time> maxTime) maxTime=time;
			System.out.println("====================================="+time);
		}
		System.out.println(maxTime);

	}
}

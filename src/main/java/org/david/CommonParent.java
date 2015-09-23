package org.david;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommonParent {

	private static class Level {
		private long start;
		private long end;
		private int n;

		Level(int n, long start, long end) {
			this.n = n;
			this.start = start;
			this.end = end;
		}

		long getStart() {
			return start;
		}

		long getEnd() {
			return end;
		}

		int getN() {
			return n;
		}

		public String toString() {
			return "n=" + n + " start=" + start + " end=" + end;
		}
	}

	private static class Element {
		private long id;
		private Level level;

		Element(long id) {
			this.id = id;
		}

		Element(long id, Level level) {
			this.id = id;
			this.level = level;
		}

		void setLevel(Level level) {
			this.level = level;
		}

		Level getLevel() {
			return level;
		}

		long getId() {
			return id;
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof Element))
				return false;
			if (obj == this)
				return true;
			Element other = (Element) obj;
			return other.id == this.id;
		}
	}

	private static List<Level> getParentLevels(long id) {
		List<Level> levels = new ArrayList<Level>();
		levels.add(new Level(0, 0, 0));
		long start = 0;
		long end = 0;
		int n = 0;
		while (id <= start || id >= end) {
			n++;
			start = end + 1;
			end = end + n3(n);
			levels.add(new Level(n, start, end));
		}
		return levels;
	}

	private static long n3(int n) {
		long result = 1;
		for (int i = 0; i < n; i++) {
			result = result * 3;
		}
		return result;
	}

	private static void identifyLevel(Element e, List<Level> levels) {
		for (int i = levels.size() - 1; i >= 0; i--) {
			Level level = levels.get(i);
			if (level.getStart() <= e.getId() && level.getEnd() >= e.getId()) {
				e.setLevel(level);
				return;
			}
		}
	}

	private static List<Element> getParentElements(Element element,
			List<Level> levels) {
		List<Element> parentElements = new ArrayList<Element>();

		int n = element.getLevel().getN();
		long id = element.getId();
		for (int i = n; i >= 1; i--) {
			Level currentLevel = levels.get(i);
			Level previousLevel = levels.get(i - 1);
			long distance = id - currentLevel.getStart();
			long x = distance / 3;
			long parentId = previousLevel.getStart() + x;
			id = parentId;
			parentElements.add(new Element(parentId, previousLevel));

		}
		return parentElements;
	}

	public static void main(String[] arg) {
		Scanner s = new Scanner(System.in);
		long first = s.nextLong();
		long second = s.nextLong();
		s.close();
		if (first == second)
			System.out.println(first);
		long bigger = first > second ? first : second;
		long smaller = first > second ? second : first;
		List<Level> levels = getParentLevels(bigger);
		Element smallerElement = new Element(smaller);
		Element biggerElement = new Element(bigger);
		identifyLevel(smallerElement, levels);
		identifyLevel(biggerElement, levels);

		List<Element> smallerParents = getParentElements(smallerElement, levels);
		List<Element> biggerParents = getParentElements(biggerElement, levels);
		for (int i = 0; i <= biggerParents.size() - 1; i++) {
			Element element = biggerParents.get(i);
			if (smallerParents.contains(element)) {
				System.out.println(element.getId());
				break;
			}
		}

	}

}

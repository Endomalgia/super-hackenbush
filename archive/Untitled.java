private static Random rng = new Random();

public static String randomize(String in) {
  if (in.length() == 0) {return "";}
  if (in == null) {return "";}
  int pos = rng.nextInt(in.length());
  String next_sub = in.substring(0,pos) + in.substring(pos+1,in.length());
  return in.charAt(pos) + randomize(next_sub);
}


public static ArrayList<ArrayList<Integer>> toN(Integer n) {
  ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
  ArrayList<Integer> elem = new ArrayList<Integer>();
  elem.add(n);
  map.add(elem);
  if (n <= 1) {return map;}
  for (int i=1; i<n; i++) {
    ArrayList<ArrayList<Integer>> map_c = toN(n-i);
    for (ArrayList<Integer> ali : map_c) {
      Boolean is_viable = true;
      for (Integer e : ali) {
        if (i<e) {
          is_viable = false;;
        }
      }
      if (!is_viable) {continue;}
      ali.add(i);
      map.add(ali);
    }
  }
  return map;
}

package DSA;

import java.util.*;

class RuleRecord {
    String facility;
    String rule;
    String status;
    String date;

    RuleRecord(String facility, String rule, String status, String date) {
        this.facility = facility;
        this.rule = rule;
        this.status = status;
        this.date = date;
    }
}

public class RuleAdherenceSystem {

    // Linked List → storage
    static LinkedList<RuleRecord> records = new LinkedList<>();

    // Stack → undo
    static Stack<RuleRecord> undoStack = new Stack<>();

    // Queue → processing simulation
    static Queue<RuleRecord> processingQueue = new LinkedList<>();

    // HashMap → rule violation frequency
    static HashMap<String, Integer> ruleFrequency = new HashMap<>();

    // HashMap → facility analytics
    static HashMap<String, int[]> facilityStats = new HashMap<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n=================================================");
            System.out.println("        RULE ADHERENCE MONITORING SYSTEM");
            System.out.println("=================================================");
            System.out.println("1. Add Record");
            System.out.println("2. Undo Last Entry");
            System.out.println("3. Show All Records");
            System.out.println("4. Search Rule / Facility");
            System.out.println("5. Sort by Date");
            System.out.println("6. Overall Consistency");
            System.out.println("7. Weekly Report");
            System.out.println("8. Monthly Facility Report");
            System.out.println("9. Most Violated Rule");
            System.out.println("10. Facility Analytics");
            System.out.println("11. Rule Stability Analysis");
            System.out.println("12. Exit");

            System.out.print("\nEnter your choice (1-12): ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: addRecord(sc); break;
                case 2: undoRecord(); break;
                case 3: displayRecords(); break;
                case 4: searchRuleOrFacility(sc); break;
                case 5: sortRecords(); break;
                case 6: overallConsistency(); break;
                case 7: weeklyReport(); break;
                case 8: monthlyFacilityReport(); break;
                case 9: mostViolatedRule(); break;
                case 10: facilityAnalytics(); break;
                case 11: ruleStabilityAnalysis(); break;
                case 12:
                    System.out.println("System terminated.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ADD RECORD
    static void addRecord(Scanner sc) {

        System.out.print("Enter Facility: ");
        String facility = sc.nextLine();

        System.out.print("Enter Rule: ");
        String rule = sc.nextLine();

        System.out.print("Enter Status (Followed/Violated): ");
        String status = sc.nextLine();

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        RuleRecord record = new RuleRecord(facility, rule, status, date);

        records.add(record);
        undoStack.push(record);
        processingQueue.add(record);

        if (status.equalsIgnoreCase("Violated")) {
            ruleFrequency.put(rule, ruleFrequency.getOrDefault(rule, 0) + 1);
        }

        facilityStats.putIfAbsent(facility, new int[]{0, 0});

        if (status.equalsIgnoreCase("Followed"))
            facilityStats.get(facility)[0]++;
        else
            facilityStats.get(facility)[1]++;

        System.out.println("Record added successfully.");
    }

    // UNDO
    static void undoRecord() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        RuleRecord last = undoStack.pop();
        records.remove(last);
        System.out.println("Last entry removed.");
    }

    // DISPLAY
    static void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No records available.");
            return;
        }
        for (RuleRecord r : records) {
            System.out.println(r.facility + " | " + r.rule + " | " + r.status + " | " + r.date);
        }
    }

    // SEARCH
    static void searchRuleOrFacility(Scanner sc) {

        System.out.print("Enter rule or facility: ");
        String key = sc.nextLine();

        boolean found = false;

        for (RuleRecord r : records) {
            if (r.rule.equalsIgnoreCase(key) || r.facility.equalsIgnoreCase(key)) {
                System.out.println(r.facility + " | " + r.rule + " | " + r.status + " | " + r.date);
                found = true;
            }
        }

        if (!found)
            System.out.println("No match found.");
    }

    // SORT
    static void sortRecords() {

        for (int i = 0; i < records.size(); i++) {
            for (int j = 0; j < records.size() - i - 1; j++) {

                if (records.get(j).date.compareTo(records.get(j + 1).date) > 0) {

                    RuleRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }

        System.out.println("Records sorted chronologically.");
    }

    // CONSISTENCY
    static void overallConsistency() {

        int followed = 0, violated = 0;

        for (RuleRecord r : records) {
            if (r.status.equalsIgnoreCase("Followed")) followed++;
            else violated++;
        }

        int total = followed + violated;

        if (total == 0) {
            System.out.println("No data available.");
            return;
        }

        double consistency = (followed * 100.0) / total;

        System.out.printf("Overall Consistency: %.2f%%\n", consistency);
    }

    // WEEKLY REPORT
    static void weeklyReport() {
        System.out.println("Weekly report generated based on recent records.");
        overallConsistency();
    }

    // MONTHLY REPORT
    static void monthlyFacilityReport() {

        for (String facility : facilityStats.keySet()) {

            int[] stats = facilityStats.get(facility);
            int total = stats[0] + stats[1];

            double consistency = (stats[0] * 100.0) / total;

            System.out.println("\nFacility: " + facility);
            System.out.printf("Consistency: %.2f%%\n", consistency);
        }
    }

    // HEAP
    static void mostViolatedRule() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(ruleFrequency.entrySet());

        if (pq.isEmpty()) {
            System.out.println("No violations recorded.");
            return;
        }

        System.out.println("Most Violated Rule: " + pq.peek().getKey());
    }

    // FACILITY ANALYTICS
    static void facilityAnalytics() {

        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) ->
                        facilityStats.get(b)[1] - facilityStats.get(a)[1]);

        for (String facility : facilityStats.keySet()) {

            int[] stats = facilityStats.get(facility);
            int total = stats[0] + stats[1];

            double consistency = (stats[0] * 100.0) / total;

            System.out.println("\nFacility: " + facility);
            System.out.printf("Consistency: %.2f%%\n", consistency);

            pq.add(facility);
        }

        System.out.println("\nPriority Ranking (Most Violations):");
        while (!pq.isEmpty())
            System.out.println(pq.poll());
    }

    // STABILITY
    static void ruleStabilityAnalysis() {

        System.out.println("\nRule Stability:");

        for (String rule : ruleFrequency.keySet()) {
            int violations = ruleFrequency.get(rule);

            if (violations <= 2)
                System.out.println(rule + " → Stable");
            else
                System.out.println(rule + " → Unstable");
        }
    }
}
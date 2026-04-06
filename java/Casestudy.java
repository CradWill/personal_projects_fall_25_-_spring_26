package com.example;

public class Casestudy {

    // Flight data
    private static final String[] FLIGHT_IDS = {
            "AA101", "UA204", "DL331", "SW512", "AA202",
            "BA123", "AF456", "UA305", "DL412", "SW613"
    };

    private static final String[] ARRIVAL_STR = {
            "06:30", "06:55", "07:10", "07:45", "08:05",
            "08:20", "09:00", "09:40", "10:10", "10:45"
    };

    private static final String[] DEPARTURE_STR = {
            "07:40", "08:10", "08:30", "09:10", "09:50",
            "10:00", "10:30", "11:00", "11:50", "12:15"
    };

    private static int timeToMinutes(String t) {
        String[] parts = t.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private static void assignGates(int numGates) {
        int n = FLIGHT_IDS.length;

        int[] arrivals = new int[n];
        int[] departures = new int[n];

        // Convert times to minutes
        for (int i = 0; i < n; i++) {
            arrivals[i] = timeToMinutes(ARRIVAL_STR[i]);
            departures[i] = timeToMinutes(DEPARTURE_STR[i]);
        }

        int[] gateAvailable = new int[numGates];  // when each gate is next free
        String[] assignedGate = new String[n];

        int totalTarmacTime = 0;
        int remoteCount = 0;
        final int MAX_TIME_ON_TARMAC_ALLOWED = 60; // minutes

        // Assign each flight
        for (int i = 0; i < n; i++) {
            int bestGate = -1;
            int bestTarmac = Integer.MAX_VALUE;

            // Find gate with minimum tarmac delay
            for (int g = 0; g < numGates; g++) {
                int tarmac = Math.max(0, gateAvailable[g] - arrivals[i]);
                if (tarmac < bestTarmac) {
                    bestTarmac = tarmac;
                    bestGate = g;
                }
            }

            // Decide if we can use that gate or must go remote
            if (bestGate != -1 && bestTarmac <= MAX_TIME_ON_TARMAC_ALLOWED) {
                assignedGate[i] = "Gate " + (bestGate + 1);
                totalTarmacTime += bestTarmac;
                gateAvailable[bestGate] = departures[i];
            } else {
                assignedGate[i] = "REMOTE";
                remoteCount++;
            }
        }

        // Output results
        System.out.println("Flight Assignments:");
        for (int i = 0; i < n; i++) {
            System.out.println(FLIGHT_IDS[i] + " -> " + assignedGate[i]);
        }

        System.out.println("\nTotal Tarmac Time: " + totalTarmacTime + " minutes");
        System.out.println("Flight Time: 41 hours");
        System.out.println("Remote Assignments: " + remoteCount);
    }

    public static void main(String[] args) {
        int numGates = 3;
        assignGates(numGates);
    }
}

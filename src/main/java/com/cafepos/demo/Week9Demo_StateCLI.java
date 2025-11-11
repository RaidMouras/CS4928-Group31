package com.cafepos.demo;

import com.cafepos.state.OrderFSM;
import java.util.Scanner;

/**
 * Interactive CLI for the Order State Machine demo.
 * Lets you manually trigger transitions and see what’s allowed.
 */
public final class Week9Demo_StateCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderFSM fsm = new OrderFSM();

        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println(" Café POS - Order Lifecycle");
        System.out.println("══════════════════════════════════════════════════════════════");

        while (true) {
            System.out.println("\nCurrent Status = " + fsm.status());
            System.out.println("Available actions: pay, prepare, ready, deliver, cancel, quit");
            System.out.print("> ");
            String cmd = sc.nextLine().trim().toLowerCase();

            switch (cmd) {
                case "pay" -> fsm.pay();
                case "prepare" -> fsm.prepare();
                case "ready" -> fsm.markReady();
                case "deliver" -> fsm.deliver();
                case "cancel" -> fsm.cancel();
                case "quit" -> {
                    System.out.println("Exiting Order FSM CLI.");
                    return;
                }
                default -> System.out.println("Unknown command: " + cmd);
            }
        }
    }
}

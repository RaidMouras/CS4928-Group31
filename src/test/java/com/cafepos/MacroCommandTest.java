package com.cafepos;

import com.cafepos.command.AddItemCommand;
import com.cafepos.command.MacroCommand;
import com.cafepos.command.OrderService;
import com.cafepos.domain.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MacroCommandTest {

    @Test
    void execute_runsAllCommands() {
        Order order = new Order(2001);
        OrderService service = new OrderService(order);

        AddItemCommand add1 = new AddItemCommand(service, "ESP", 1);
        AddItemCommand add2 = new AddItemCommand(service, "LAT", 1);

        MacroCommand macro = new MacroCommand(add1, add2);
        int before = order.items().size();

        macro.execute();
        int after = order.items().size();

        assertEquals(before + 2, after, "Macro should execute both commands");
    }

    @Test
    void undo_reversesCommandsInReverseOrder() {
        Order order = new Order(2002);
        OrderService service = new OrderService(order);

        AddItemCommand add1 = new AddItemCommand(service, "ESP", 1);
        AddItemCommand add2 = new AddItemCommand(service, "LAT", 1);

        MacroCommand macro = new MacroCommand(add1, add2);

        macro.execute();
        int afterExecute = order.items().size();

        macro.undo();  // should undo both adds in reverse
        int afterUndo = order.items().size();

        assertEquals(afterExecute - 2, afterUndo, "Undo should remove both items");
    }
}

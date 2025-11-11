package com.cafepos;

import com.cafepos.command.AddItemCommand;
import com.cafepos.command.OrderService;
import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddItemCommandTest {

    @Test
    void execute_addsItemToOrder() {
        Order order = new Order(1001);
        OrderService service = new OrderService(order);
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 2);

        int before = order.items().size();
        cmd.execute();
        int after = order.items().size();

        assertEquals(before + 1, after, "Item should be added to the order");
    }

    @Test
    void undo_removesLastItem() {
        Order order = new Order(1002);
        OrderService service = new OrderService(order);
        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);

        cmd.execute(); // add item
        int mid = order.items().size();

        cmd.undo();    // remove item
        int finalCount = order.items().size();

        assertEquals(mid - 1, finalCount, "Undo should remove the last item added");
    }
}

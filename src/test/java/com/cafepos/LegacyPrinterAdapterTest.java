package com.cafepos;

import com.cafepos.printing.LegacyPrinterAdapter;
import com.cafepos.printing.Printer;
import vendor.legacy.LegacyThermalPrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FakeLegacyPrinter extends LegacyThermalPrinter {
    int lastLength = 0;

    @Override
    public void legacyPrint(byte[] payload) {
        this.lastLength = payload.length;
    }

    public int getLastLength() {
        return lastLength;
    }
}

public class LegacyPrinterAdapterTest {

    @Test
    void adapter_convertsTextToBytes() {
        FakeLegacyPrinter fakePrinter = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fakePrinter);

        adapter.print("ABC");

        assertTrue(fakePrinter.getLastLength() >= 3, "Expected at least 3 bytes for 'ABC'");
    }
}

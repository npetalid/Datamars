package gr.petalidis.datamars.inspections.domain;

import java.util.Objects;

public class UsbAlias {
    private String usb;
    private String alias;

    public UsbAlias(String usb, String alias) {
        this.usb = usb;
        this.alias = alias;
    }

    public String getUsb() {
        return usb;
    }

    public void setUsb(String usb) {
        this.usb = usb;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return this.alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsbAlias usbAlias = (UsbAlias) o;
        return Objects.equals(usb, usbAlias.usb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usb);
    }
}

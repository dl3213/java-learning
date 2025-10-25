package code.sibyl;

import java.util.function.Supplier;

public class SingleDemo {

    public static void main(String[] args) {
        int i = 0;
        while (i <= 100) {
            Holder holder = new Holder();
            Service service1 = holder.get();
            Service service2 = holder.get();
            System.err.println(service1);
            System.err.println(service2);
            System.err.println(service1 == service2);

            HolderWithSync holderWithSync = new HolderWithSync();
            Service serviceWithSync1 = holderWithSync.get();
            Service serviceWithSync2 = holderWithSync.get();
            System.err.println(serviceWithSync1);
            System.err.println(serviceWithSync2);
            System.err.println(serviceWithSync1 == serviceWithSync2);

            ServiceSupplier serviceSupplier = new ServiceSupplier();
            Service serviceBySupplier1 = serviceSupplier.get();
            Service serviceBySupplier2 = serviceSupplier.get();
            System.err.println(serviceBySupplier1);
            System.err.println(serviceBySupplier2);
            System.err.println(serviceBySupplier1 == serviceBySupplier2);
        }
    }
}

class ServiceSupplier {
    private Supplier<Service> supplier = this::createAndCache;

    public ServiceSupplier() {

    }

    public Service get() {
        return supplier.get();
    }

    private synchronized Service createAndCache() {
        class Factory implements Supplier<Service> {
            private final Service service = new Service();

            @Override
            public Service get() {
                return service;
            }
        }
        if (!(supplier instanceof Factory)) {
            supplier = new Factory();
        }
        return supplier.get();
    }
}

class HolderWithSync {
    private Service service;

    public synchronized Service get() {
        if (service == null) {
            this.service = new Service();
            return this.service;
        }
        return service;
    }
}

class Holder {
    private Service service;

    public Service get() {
        if (service == null) {
            this.service = new Service();
            return this.service;
        }
        return service;
    }
}


class Service {
    public Service() {

    }
}
package code.sibyl.service.torrent;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BitTorrents {

    public static void main(String[] args) throws Exception {

        String magnetLink = "magnet:?xt=urn:btih:AF50EB77245AEAB69908D46FFF7FBF36355CB8AB&dn=Rio%28Idea+Pocket%29Rio-DIGITAL+CHANNEL%28SUPD-077%29FULLHD";

        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };

// enable bootstrapping from public routers
//        Module dhtModule = new DHTModule(new DHTConfig() {
//            @Override
//            public boolean shouldUseRouterBootstrap() {
//                return true;
//            }
//        });

// get download directory
        Path targetDirectory = Paths.get("D:\\");
//        Path targetDirectory = Paths.get(System.getProperty("user.home"), "Downloads");

// create file system based backend for torrent data
        Storage storage = new FileSystemStorage(targetDirectory);

// create client with a private runtime
        BtClient client = Bt.client()
                .config(config)
                .storage(storage)
                .magnet(magnetLink)
                .autoLoadModules()
//                .module(dhtModule)
                .stopWhenDownloaded()
                .build();

// launch
        client.startAsync().join();

    }

}
package co.nano.nanowallet.model;

import java.util.Arrays;
import java.util.List;

import co.nano.nanowallet.NanoUtil;
import co.nano.nanowallet.util.ExceptionHandler;
import io.realm.RealmObject;

/**
 * Wallet seed used to store in Realm
 */

public class Credentials extends RealmObject {
    private String seed;
    private String privateKey;
    private String uuid;

    public static final List<Character> VALID_SEED_CHARACTERS = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    public Credentials() {
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        // validate seed length
        if (!isValidSeed(seed)) {
            ExceptionHandler.handle(new Throwable("Invalid Seed: " + seed));
            return;
        }

        this.seed = seed;
        this.privateKey = NanoUtil.seedToPrivate(seed);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    // Generated fields

    public String getPublicKey() {
        if (this.privateKey != null) {
            return NanoUtil.privateToPublic(this.privateKey);
        } else {
            return null;
        }
    }

    public Address getAddress() {
        String publicKey = getPublicKey();
        if (publicKey != null) {
            return new Address(NanoUtil.publicToAddress(publicKey));
        } else {
            return null;
        }
    }

    public String getAddressString() {
        String publicKey = getPublicKey();
        if (publicKey != null) {
            return NanoUtil.publicToAddress(publicKey);
        } else {
            return null;
        }
    }

    public static boolean isValidSeed(String seed) {
        if (seed.length() != 64) {
            return false;
        }
        boolean isMatch = true;
        for (int i = 0; i < seed.length() && isMatch; i++) {
            char letter = seed.toLowerCase().charAt(i);
            if (!VALID_SEED_CHARACTERS.contains(letter)) {
                isMatch = false;
            }
        }
        return isMatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        if (seed != null ? !seed.equals(that.seed) : that.seed != null) return false;
        if (privateKey != null ? !privateKey.equals(that.privateKey) : that.privateKey != null) {
            return false;
        }
        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
    }

    @Override
    public int hashCode() {
        int result = seed != null ? seed.hashCode() : 0;
        result = 31 * result + (privateKey != null ? privateKey.hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }
}

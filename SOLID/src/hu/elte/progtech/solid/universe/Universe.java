package hu.elte.progtech.solid.universe;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Universe {

    void printLawOfGravity();
    
    boolean isItReal(String it);
    
    BigInteger getSpeedOfLightInMph();
    
    BigInteger getSpedOfLightInKph();
    
    BigDecimal getMass(String planet);
    
}

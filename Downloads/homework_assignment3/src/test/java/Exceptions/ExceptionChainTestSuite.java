package Exceptions;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;

@Suite
@SelectClasses({
    ExceptionChainTest.class
})
public class ExceptionChainTestSuite {
    // This class remains empty. It is used only as a holder for the above annotations.
}

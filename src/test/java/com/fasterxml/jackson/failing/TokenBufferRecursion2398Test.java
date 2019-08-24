package com.fasterxml.jackson.failing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class TokenBufferRecursion2398Test extends BaseMapTest
{
    private final ObjectMapper MAPPER = sharedMapper();

    // 10k does it, 5k not
    private final static int RECURSION = 9999;
    
    public void testDeeplyNestedArrays() throws Exception
    {
        try (JsonParser p = MAPPER.tokenStreamFactory().createParser(_createNested(RECURSION * 2,
                "[", " 123 ", "]"))) {
            p.nextToken();
            TokenBuffer b = new TokenBuffer(p);
            b.copyCurrentStructure(p);
            b.close();
        }
    }

    public void testDeeplyNestedObjects() throws Exception
    {
        try (JsonParser p = MAPPER.tokenStreamFactory().createParser(_createNested(RECURSION,
                "{\"a\":", "42", "}"))) {
            p.nextToken();
            TokenBuffer b = new TokenBuffer(p);
            b.copyCurrentStructure(p);
            b.close();
        }
    }

    private String _createNested(int nesting, String open, String middle, String close) 
    {
        StringBuilder sb = new StringBuilder(2 * nesting);
        for (int i = 0; i < nesting; ++i) {
            sb.append(open);
        }
        sb.append(middle);
        for (int i = 0; i < nesting; ++i) {
            sb.append(close);
        }
        return sb.toString();
    }
}
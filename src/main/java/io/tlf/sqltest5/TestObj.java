package io.tlf.sqltest5;

import com.impossibl.postgres.api.jdbc.PGAnyType;
import java.sql.JDBCType;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class TestObj implements SQLData {
  private static final String TYPE_NAME = "public.test_obj";

  public static final PGAnyType TYPE = new PGAnyType() {
    @Override
    public String getName() {
      return TestObj.TYPE_NAME;
    }

    @Override
    public String getVendor() {
      return "UDT Generated";
    }

    @Override
    public Integer getVendorTypeNumber() {
      return null;
    }

    @Override
    public Class getJavaType() {
      return TestObj.class;
    }
  };

  private String[] strings;

  @Override
  public String getSQLTypeName() throws SQLException {
    return TYPE_NAME;
  }

  public String[] getStrings() {
    return strings;
  }

  public void setStrings(String[] strings) {
    this.strings = strings;
  }

  @Override
  public void readSQL(SQLInput in, String typeName) throws SQLException {
    this.strings = in.readObject(String[].class);
  }

  @Override
  public void writeSQL(SQLOutput out) throws SQLException {
    out.writeObject(this.strings, JDBCType.ARRAY);
  }
}

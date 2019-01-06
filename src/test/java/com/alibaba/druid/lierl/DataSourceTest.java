package com.alibaba.druid.lierl;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSourceTest {

    @Test
    public void testDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://139.199.129.251:3306/canal_es");
        //配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。
        //如果没有配置，将会生成一个名字，格式是："DataSource-" + System.identityHashCode(this)
        dataSource.setName("datasource");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        //初始化发生在显示调用init方法，或者第一次getConnection时
        dataSource.setInitialSize(10);//初始化线程池大小，默认：0
        dataSource.setMinIdle(3);//最小连接池数量
        dataSource.setMaxActive(50);//最大连接池数量，默认：8
        //获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，
        // 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
        dataSource.setMaxWait(60000);//最大等待时间
        //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭
        dataSource.setPoolPreparedStatements(false);
        //要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，
        // 不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
        dataSource.setMaxOpenPreparedStatements(-1);
        //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，
        // testOnBorrow、testOnReturn、testWhileIdle都不会其作用。
        dataSource.setValidationQuery("select 1");
        //申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。默认：true
        dataSource.setTestOnBorrow(true);
        //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,默认：false
        dataSource.setTestOnReturn(false);
        //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，
        // 执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(true);
        // 1) Destroy线程会检测连接的间隔时间
        // 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        //
        dataSource.setMinEvictableIdleTimeMillis(300000);
        //物理连接初始化的时候执行的sql
        List<String> sqls = new ArrayList<String>();
        sqls.add("set names utf8mb4");
        dataSource.setConnectionInitSqls(sqls);
        //当数据库抛出一些不可恢复的异常时，抛弃连接,默认根据dbtype自动识别
        //dataSource.setExceptionSorter();
        dataSource.setFilters("config,log4j2,wall");
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(3000);
        statFilter.setMergeSql(true);
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(statFilter);
        dataSource.setProxyFilters(filters);

        dataSource.init();
    }
}

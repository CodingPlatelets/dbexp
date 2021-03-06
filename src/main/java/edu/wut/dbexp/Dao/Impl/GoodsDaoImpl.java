package edu.wut.dbexp.Dao.Impl;

import edu.wut.dbexp.DataObject.Good;
import edu.wut.dbexp.Utils.DruidUtil;
import edu.wut.dbexp.Dao.GoodsDao;
import edu.wut.dbexp.DataObject.Goods;
import edu.wut.dbexp.Utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenkan
 * @date 2021/5/21 21:57
 */
@Repository("GoodsDao")
public class GoodsDaoImpl implements GoodsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GoodsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean addGoods(Goods goods) {
        String sql = "insert into goods (goodAttributes, description, originPrice) values(?,?,?)";
        try {
            return jdbcTemplate.update(sql,
                    goods.getGoodAttributes(),
                    goods.getDescription(),
                    goods.getOriginPrice()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean updateGoods(Goods goods) {
        String sql = "update goods set description = ? , originPrice = ? , stock = ? where goodAttributes = ?";
        try {
            return jdbcTemplate.update(sql,
                    goods.getDescription(),
                    goods.getOriginPrice(),
                    goods.getStock(),
                    goods.getGoodAttributes()
            ) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean updateGoodStock(int goodsAttribute, int stock) {
        String sql = "update goods set stock = ? where goodAttributes = ?";
        try {
            return jdbcTemplate.update(sql,
                    stock,
                    goodsAttribute
            ) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteGood(String goodId) {
        String sql = "delete from good where goodId = ?";
        try{
            return jdbcTemplate.update(sql,goodId) == 1;
        }catch (DataAccessException dataAccessException){
            return false;
        }
    }

    @Override
    public Good searchGood(String goodId) {
        String sql = "select * from good where goodId = ?";
        try{
            return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Good.class),goodId);
        }catch (DataAccessException dataAccessException){
            return null;
        }
    }

    @Override
    public boolean updateGood(Good good) {
        String sql = "update good set goodAttributes = ? ,saleDate = ? ,isSale = ? ,salePrice = ?where goodId = ?";
        try {
            return jdbcTemplate.update(sql,
                    good.getGoodAttributes(),
                    good.getSaleDate(),
                    good.getIsSale(),
                    good.getSalePrice(),
                    good.getGoodId()
            ) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean insertGood(int goodAttributes) {
        String sql = "insert good (goodAttributes,goodId) values(?,?)";
        try{
            return jdbcTemplate.update(sql,goodAttributes, IdUtils.getPrimaryKey()) == 1;
        }catch (DataAccessException d){
            return false;
        }
    }



    @Override
    public List<Goods> getAllGoods() {
        try {
            return jdbcTemplate.query(
                    "select * from goods", new BeanPropertyRowMapper<>(Goods.class));
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    @Override
    public List<Goods> selectedByStock(int num) {
        try {
            return jdbcTemplate.query(
                    "select * from goods where stock< ?", new BeanPropertyRowMapper<>(Goods.class), num);
        } catch (DataAccessException dataAccessException) {
            return null;
        }
    }

    @Override
    public List<Good> getAllGoodFromAttribute(int attribute) {
        try {
            return jdbcTemplate.query(
                    "select * from good where goodAttributes = ?;",
                    new BeanPropertyRowMapper<>(Good.class)
            );
        }catch (DataAccessException d){
            return null;
        }
    }

    @Override
    public Goods searchGoods(int goodsAttribute) {
        String sql="select * from goods where goodAttributes=?";
        try {
            return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Goods.class),goodsAttribute);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Good queryOneGoodByAttributes(int goodAttributes) {
        String sql="select * from good where goodAttributes = ? and isSale = false";
        try{
            List<Good> good = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Good.class), goodAttributes);
            return good.get(0);
        }
        catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public boolean refundGood(Good good) {
        return System.currentTimeMillis()-good.getSaleDate().getTime() < 7*24*60*60*1000;
    }

    @Override
    public List<Good> getAllGood(){
        String sql="select * from good where isSale = true";
        try{
            return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Good.class));
        }
        catch (DataAccessException e){
            return null;
        }
    }
    @Override
    public List<Goods> searchGoodsMH(Integer goodAttributes) {
        String sql="select * from goods where goodAttributes like CONCAT('%',?,'%')";
        try {
            return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Goods.class),goodAttributes);
        }
        catch (DataAccessException e) {
            return null;
        }
    }
}


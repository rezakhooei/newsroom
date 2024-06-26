package com.tdxir.myapp.repository;

import com.tdxir.myapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface WkhPostMetaRepository extends JpaRepository<wkh_postmeta,Long> {
    @Query("select new Docs (c1.id,c1.userid,c1.date,c1.docName,c1.description,c1.voiceFileName,c1.imageFileName) from Docs c1 where c1.docName=:docName and c1.userid=:userId ")

    public List<Docs> reportDocs (@Param("docName") String docName,@Param("userId") String userId);
    @Query("select new Company (c1.id,c1.ownerName,c1.branch) from Company c1 where c1.ownerName=:userId ")

    public List<Company> reportCompanies (@Param("userId") String userId);
    @Query("select w1.post_id from wkh_postmeta w1 where w1.meta_key='_sku' and w1.meta_value=:code")

    public String existsCode (@Param("code") String code);

    @Query("select B1.idDoc from Invoices B1 where B1.idInvoice=:code")

    public Integer existsCodeInvoice (@Param("code") String code);

    @Query("select B1.idInvoice from Invoices B1 where B1.idInvoice=:code")

    public String idInvoice (@Param("code") String code);

    @Query("select B1.price from Invoices B1 where B1.idInvoice=:code")

    public Long priceOfInvoice (@Param("code") String code);
    @Query("select B1.price from Bills B1 where B1.idInvoice=:code")

    public List<Long> priceOfInvoiceBill (@Param("code") String code);

    @Query("select w1.post_id from wkh_postmeta w1 where w1.meta_key='_sku' and w1.meta_value=:mahakCode")

        public long post_id (@Param("mahakCode") String mahakCode);
    @Query("select w1.meta_value from wkh_postmeta w1 where w1.post_id=:post_id and w1.meta_key='_price'")

    public long price (@Param("post_id") String post_id);
    @Query("select wm1.meta_value  from wkh_postmeta wm1 where wm1.meta_key='_price' and wm1.post_id in (select wm2.post_id from wkh_postmeta wm2 where wm2.meta_key='_sku' and wm2.meta_value=:code)")

    public List<String> priceIdCode (@Param("code") String code);
    @Query("select new BuyData(bd.id,bd.email,bd.date,bd.sku,bd.stock,bd.oldStock,bd.price,bd.oldPrice,bd.idInvoice,bd.companyId) from BuyData as bd where  bd.idInvoice=:idInvoice order by bd.id ")

    public List<BuyData> findInvoiceInBuyData (@Param("idInvoice") String idInvoice);
    @Query("select new BuyData(bd.id,bd.email,bd.date,bd.sku,bd.stock,bd.oldStock,bd.price,bd.oldPrice,bd.idInvoice,bd.companyId) from BuyData as bd where bd.sku=:code and bd.idInvoice=:idInvoice order by bd.id ")

    public List<BuyData> findSkuInInvoice (@Param("code") String code,@Param("idInvoice") String idInvoice);
    @Query("select bd.price from BuyData as bd where bd.sku=:code order by bd.id ")

    public List<Long> buyPrice (@Param("code") String code);
    @Query("select new BuyData(bd.id,bd.email,bd.date,bd.sku,bd.stock,bd.oldStock,bd.price,bd.oldPrice,bd.idInvoice,bd.companyId) from BuyData bd where bd.sku=:code order by bd.id")
    public List<BuyData> reportProduct (@Param("code") String code);
    @Query("select new Invoices (bi.idDoc,bi.idInvoice,bi.userName,bi.sellerID,bi.date,bi.dateInvoice,bi.numProduct,bi.idCheck,bi.price,bi.fileImage,bi.companyId,bi.paid,bi.sellOrBuy,bi.completed) from Invoices bi where bi.idInvoice=:code and bi.companyId=:companyId order by bi.idDoc ")
    public Invoices reportInvoices (@Param("code") String code,@Param("companyId") Integer companyId);
    @Query("select new Bills (bi.id,bi.date,bi.datePay,bi.idDoc,bi.idInvoice,bi.price,bi.payKind,bi.userName,bi.fileImage,bi.finish,bi.description,bi.companyId) from Bills bi where bi.idInvoice=:code and bi.companyId=:companyId order by bi.datePay ")
    public List<Bills> reportInvoiceInBills (@Param("code") String code,@Param("companyId") Integer companyId);


    @Query("select p.post_title from WkhPosts  p where p.id in(select post_id from wkh_postmeta where meta_key='_sku' and meta_value=:code) and p.post_type='product'")

    public List<String> nameIdCode (@Param("code") String code);

    @Query("select wm1.meta_value  from wkh_postmeta wm1 where wm1.meta_key='_stock' and wm1.post_id in (select wm2.post_id from wkh_postmeta wm2 where wm2.meta_key='_sku' and wm2.meta_value=:code)")

    public List<String> stockIdCode (@Param("code") String code);

    @Query("select wm1.post_id  from wkh_postmeta wm1 where wm1.meta_key='_sku' and wm1.meta_value=:code")

    public List<String> postIdCode (@Param("code") String code);
    @Query("select meta_value from wkh_postmeta where post_id in(select meta_value from wkh_postmeta where\n" +
            "meta_key='_thumbnail_id' and post_id in(select post_id from wkh_postmeta where meta_key='_sku' and meta_value=:id))\n" +
            "and meta_key='_wp_attached_file'")
    public String imageUrlId(@Param("id") String id);
    @Query("select BI.fileImage from Invoices BI where BI.idInvoice=:idInvoice")
    public List<String> imageUrlInvoice(@Param("idInvoice") String idInvoice);
    @Query("select  pm2.meta_value from wkh_postmeta pm2 where pm2.meta_key='_thumbnail_id' and pm2.post_id in(select pm3.post_id from wkh_postmeta pm3 where pm3.meta_key='_sku' and pm3.meta_value=:code)")
    public List<String> findThumbnail(@Param("code") String code);
    @Modifying
    @Transactional
    @Query(value="update bills b set b.price=:newPrice  where b.id_doc=:idDoc and b.id_invoice=:idInvoice and b.pay_kind=:payKind and b.description=:description and b.id_check=:idCheck", nativeQuery = true )


    public Integer updateBills(@Param("idDoc") Long idDoc,@Param("idInvoice") String idInvoice,@Param("newPrice") Long newPrice,@Param("payKind") String payKind ,
            @Param("description") String description,@Param("idCheck") Long idCheck);




    @Modifying
    @Transactional
    @Query(value="update buy_data bd set bd.stock=:newStock,bd.price=:newPrice where bd.id=:id", nativeQuery = true )


    public Integer updateBuyData(@Param("newStock") Long newStock,@Param("newPrice") Long newPrice,@Param("id") Long id );

    @Modifying
    @Transactional
    @Query(value="update invoices bi set bi.num_product=:numProduct,bi.price=:price  where bi.id_invoice=:idInvoices", nativeQuery = true )


    public Integer updateInvoices(@Param("numProduct") Long numProduct,@Param("price") Long price,@Param("idInvoices") String idInvoices );
    @Modifying
    @Transactional
    @Query(value="update invoices bi set bi.completed=:completed where bi.id_invoice=:idInvoices", nativeQuery = true )


    public Integer updateInvoicesCompleted(@Param("completed") Boolean completed,@Param("idInvoices") String idInvoices );
    @Modifying
    @Transactional
    @Query(value="update invoices bi set bi.paid=:paid where bi.id_invoice=:idInvoices", nativeQuery = true )


    public Integer updateInvoicesPaid(@Param("paid") Boolean paid,@Param("idInvoices") String idInvoices );
    @Modifying
    @Transactional
    @Query(value="update wkh_postmeta pm1 set pm1.meta_value=:fileName where pm1.meta_key='_wp_attached_file' and pm1.post_id=:postId", nativeQuery = true )


    public Integer updateImage(@Param("fileName") String fileName,@Param("postId") String PostId );

    @Modifying
    @Transactional
    @Query(value="update wkh_postmeta  SET meta_value=:stock  WHERE  meta_key='_stock' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateStock(@Param("stock") String stock,@Param("postId") String postId );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:price  WHERE  meta_key='_price' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updatePrice(@Param("price") String price,@Param("postId") String postId );

    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:price  WHERE  meta_key='_regular_price' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateRegularPrice(@Param("price") String price,@Param("postId") String postId );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:price  WHERE  meta_key='_sale_price' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateSalePrice(@Param("price") String price,@Param("postId") String postId );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:price  WHERE  meta_key='wcwp_wholesale' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateWholeSalePrice(@Param("price") String price,@Param("postId") String postId );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:status WHERE  meta_key='_stock_status' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateStockStatus(@Param("status") String status,@Param("postId") String postId );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:status WHERE  meta_key='_manage_stock' and  (post_id =:postId )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public Integer updateManageStock(@Param("status") String status,@Param("postId") String postId );

    @Modifying
    @Transactional
    @Query(value="update wkh_postmeta  SET meta_value=:mahakStock  WHERE  meta_key='_stock' and  (post_id =:mahakCode )", nativeQuery = true )

        //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public void updateStockMahak(@Param("mahakStock") String mahakStock,@Param("mahakCode") String mahakCode );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="update wkh_postmeta  SET meta_value=:mahakPrice  WHERE  meta_key='_price' and  (post_id =:mahakCode )", nativeQuery = true )

    //    ("UPDATE WkhPostMeta as w1 , (SELECT post_id  FROM WkhPostMeta WHERE meta_key='_sku' and meta_value='902') AS w2 SET w1.meta_value = '48' WHERE w1.post_id=w2.post_id")
    public void updatePriceMahak(@Param("mahakPrice") String mahakPrice,@Param("mahakCode") String mahakCode );
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into company (branch,owner_name) values (:branch,:userId) ",nativeQuery = true)

    public Integer insertCompanyId(@Param("userId") String userId,@Param("branch") String branch);
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'_sku',:mahakCode) ",nativeQuery = true)

    public void insertSku(@Param("post_id") Long postid,@Param("mahakCode") String mahakCode);

    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value=" insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'_stock',:mahakStock)",nativeQuery = true)
    public void insertStock(@Param("post_id") Long postid,@Param("mahakStock") String mahakStock);
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'_price',:mahakPrice)",nativeQuery = true)
    public void insertPrice(@Param("post_id") Long postid,@Param("mahakPrice") String mahakPrice);
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'_regular_price',:mahakRegularPrice)",nativeQuery = true)
    public void insertRegularPrice(@Param("post_id") Long postid,@Param("mahakRegularPrice") String mahakRegularPrice);
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'_sale_price',:mahakRegularPrice)",nativeQuery = true)
    public void insertSalePrice(@Param("post_id") Long postid,@Param("mahakRegularPrice") String mahakRegularPrice);

    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into Wkh_postmeta (post_id,meta_key,meta_value) values (:post_id,'wcwp_wholesale',:mahakRegularPrice)",nativeQuery = true)
    public void insertWholeSalePrice(@Param("post_id") Long postid,@Param("mahakRegularPrice") String mahakRegularPrice);
    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into wkh_term_relationships (object_id,term_taxonomy_id,term_order) values (:post_id,:term_taxonomy_id,0)",nativeQuery = true)
    public void insertCategory(@Param("post_id") Long post_id,@Param("term_taxonomy_id") Integer term_taxonomy_id);

    @Modifying
    @Transactional                 //   _price , _sale_price  , _regular_price , wcwp_wholesale
    @Query(value="insert into buy_data (date,email,sku,stock,old_stock,price,old_price,id_invoice) values (:date,:email,:sku,:stock,:oldStock,:price,:oldPrice,:idInvoice)",nativeQuery = true)
    public Integer insertBuyData(@Param("date") String date,@Param("email") String email,@Param("sku") String sku,@Param("stock") Integer stock,
                                 @Param("oldStock") Integer oldStock,@Param("price") Long price,@Param("oldPrice") Long oldPrice,
                                @Param("idInvoice") String idInvoice);
    //nowDate, userName, numProduct, price, sellerId
    @Modifying
    @Transactional
    @Query(value="insert into invoices (id_invoice,user_name,file_image,date,date_invoice,num_product,price,sellerId,company_id,paid,sell_or_buy) values (:idInvoice,:userName,:fileName,:date,:dateInvoice,:numProduct,:price,:sellerId,:companyId,:paid,:sellOrBuy)",nativeQuery = true)
    public Integer insertInvoice(@Param("idInvoice") String idInvoice, @Param("userName") String userName, @Param("fileName") String fileName, @Param("date") String date, @Param("dateInvoice") LocalDate dateInvoice, @Param("numProduct") Long numProduct,
                                 @Param("price") Long price, @Param("sellerId") String sellerId,@Param("companyId") Integer companyId,@Param("paid") Boolean paid,@Param("sellOrBuy") String sellOrBuy);

    @Modifying
    @Transactional
    @Query(value="insert into bills (date,date_pay,id_doc,id_invoice,price,pay_kind,user_name,file_image,finish,description,company_id) values (:date,:dateInvoice,:idDoc,:idInvoice,:price,:payKind,:userName,:fileName,:finish,:description,:companyId)",nativeQuery = true)
    public Integer insertBills(@Param("date") String date, @Param("dateInvoice") LocalDate dateInvoice, @Param("idDoc") Long idDoc, @Param("idInvoice") String idInvoice,
                                 @Param("price") Long price,  @Param("payKind") String PayKind, @Param("userName") String userName, @Param("fileName") String fileName,@Param("finish") Boolean finish,@Param("description") String description,@Param("companyId") Integer companyId);

    @Modifying
    @Transactional


    @Query(value="insert into docs (userid,date,doc_name,description,voice_file_name,image_file_name) values (:userName,:date,:docName,:description,:fileNameVoice ,:fileNameImg)",nativeQuery = true)

    public Integer insertDoc(@Param("userName") String userName,@Param("date") String date,   @Param("docName") String docName,
            @Param("description") String description,  @Param("fileNameVoice") String fileNameVoice, @Param("fileNameImg") String fileNameImg);

}

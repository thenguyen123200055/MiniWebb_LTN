/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import context.DbContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Hoa;

/**
 *
 * @author Administrator
 */
public class HoaDAO {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    public ArrayList<Hoa> getTop10() {
        ArrayList<Hoa> ds = new ArrayList<>();
        String sql = "select top 10 * from Hoa order by gia desc";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                 ds.add(new Hoa(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getDate(6)));
            }
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return ds;
    }
     public ArrayList<Hoa> getAll() {
        ArrayList<Hoa> ds = new ArrayList<>();
        String sql = "select * from Hoa";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                 ds.add(new Hoa(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getDate(6)));
            }
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return ds;
     }
     //phuong thuc them mới sản phẩm (Hoa)
    public boolean Insert (Hoa hoa)
    {
        String sql = "insert into hoa (tenhoa,gia,hinh,maloai,ngaycapnhat) values (?,?,?,?,?)";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, hoa.getTenhoa());
            ps.setDouble(2, hoa.getGia());
            ps.setString(3, hoa.getHinh());
            ps.setInt(4, hoa.getMaloai());
            ps.setDate(5, hoa.getNgaycapnhat());
            int kq = ps.executeUpdate();
            if(kq>0)
               return true;
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return false;
    }
    //phuong thuc cập nhật sản phẩm (Hoa)
    public boolean Update (Hoa hoa)
    {
        String sql = "update hoa set tenhoa=?,gia=?,hinh=?,maloai=?,ngaycapnhat=? where mahoa=?";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, hoa.getTenhoa());
            ps.setDouble(2, hoa.getGia());
            ps.setString(3, hoa.getHinh());
            ps.setInt(4, hoa.getMaloai());
            ps.setDate(5, hoa.getNgaycapnhat());
            ps.setInt(6, hoa.getMahoa());
            int kq = ps.executeUpdate();
            if(kq>0)
               return true;
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return false;
    }
     //phuong thuc xoá sản phẩm (Hoa)
    public boolean Delete (int mahoa)
    {
        String sql = "delete from hoa where mahoa=?";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);           
            ps.setInt(1, mahoa);           
            int kq = ps.executeUpdate();
            if(kq>0)
               return true;
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return false;
    } 
    
    //phuong thuc lấy thông tin sản phẩm (Hoa) theo mã hoa 
    public Hoa getById(int mahoa) {
        Hoa kq=null;
        String sql = "select * from Hoa where mahoa=?";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, mahoa);
            rs = ps.executeQuery();
            if (rs.next()) {
                kq =new Hoa(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getDate(6));
            }
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return kq;
    }
    //Phương thức đọc hoa theo thể loại
    public ArrayList<Hoa> getByCategoryId(int maloai) {
        ArrayList<Hoa> ds = new ArrayList<>();
        String sql = "select * from Hoa where maloai=?";
        conn = DbContext.getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maloai);
            rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new Hoa(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getInt(5), rs.getDate(6)));
            }
        } catch (Exception ex) {
            System.out.println("Loi:" + ex.toString());
        }
        return ds;
    }
   public List<Hoa> getPhantrangsanpham(int start, int pageSize) {
    List<Hoa> list = new ArrayList<>();
    String sql = "SELECT * FROM Hoa ORDER BY mahoa OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; 
    try (Connection conn = DbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, start);
        ps.setInt(2, pageSize);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Hoa hoa = new Hoa();
                hoa.setMahoa(rs.getInt("mahoa"));
                hoa.setTenhoa(rs.getString("tenhoa"));
                hoa.setGia(rs.getDouble("gia"));
                hoa.setHinh(rs.getString("hinh"));
                hoa.setMaloai(rs.getInt("maloai"));
                hoa.setNgaycapnhat(rs.getDate("ngaycapnhat"));
                list.add(hoa);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

public int getTongsosanpham() {
    String sql = "SELECT COUNT(*) FROM Hoa";
    try (Connection conn = DbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (Exception e) {
        System.out.println("Lỗi:"+e.toString());
    }
    return 0;
}

    
    public static void main(String[] args) {
        HoaDAO hoaDao = new HoaDAO();
        System.out.println("Lay tat ca hoa");
        ArrayList<Hoa> dsHoa=hoaDao.getAll();
        for (Hoa hoa : dsHoa) {
            System.out.println(hoa);
        }
        
        dsHoa = hoaDao.getByCategoryId(2);
        for (Hoa hoa : dsHoa) {
            System.out.println(hoa);
        }
        //tìm hoa theo mahoa=1
        System.out.println("Tim hoa co mahoa=1");
        Hoa kq = hoaDao.getById(1);
        if(kq!=null)
        {
            System.out.println(kq);
        }
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.HoaDAO;
import dao.LoaiDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Hoa;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ManageProduct", urlPatterns = {"/ManageProduct"})
@MultipartConfig
public class ManageProduct extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Số sản phẩm trên mỗi trang

        LoaiDAO loaiDAO = new LoaiDAO();
        HoaDAO hoaDAO = new HoaDAO();
        String action = "list";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        switch (action) {
            case "list":
                // Số sản phẩm trên mỗi trang
                int pageSize = 5;
                int pageNumber = 1;

                // Lấy tham số page từ request nếu có
                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    pageNumber = Integer.parseInt(pageParam);
                }

                // Tính toán vị trí bắt đầu và kết thúc
                int start = (pageNumber - 1) * pageSize;

                // Tạo đối tượng HoaDAO để lấy dữ liệu sản phẩm
                List<Hoa> productList = hoaDAO.getPhantrangsanpham(start, pageSize);

                // Tính tổng số trang
                int tongsp = hoaDAO.getTongsosanpham();
                int sotrang = (int) Math.ceil((double) tongsp / pageSize);

                // Đưa dữ liệu vào request
                request.setAttribute("productList", productList);
                request.setAttribute("currentPage", pageNumber);
                request.setAttribute("totalPages", sotrang);

                // Chuyển đến JSP để hiển thị dữ liệu
                request.getRequestDispatcher("/admin/list_product.jsp").forward(request, response);
                ArrayList<Hoa> dsHoa = hoaDAO.getAll();
                request.setAttribute("dsHoa", dsHoa);
                request.getRequestDispatcher("admin/list_product.jsp").forward(request, response);
                break;
            case "add":
                if (request.getMethod().equalsIgnoreCase("get")) {
                    request.setAttribute("dsLoai", loaiDAO.getAll());
                    request.getRequestDispatcher("admin/add_product.jsp").forward(request, response);
                } else if (request.getMethod().equalsIgnoreCase("post")) {
                    //Xử lý thêm mới sản phẩm
                    //b1.lấy thông tin sản phẩm
                    String tenhoa = request.getParameter("tenhoa");
                    double gia = Double.parseDouble(request.getParameter("gia"));
                    Part part = request.getPart("hinh");
                    int maloai = Integer.parseInt(request.getParameter("maloai"));

                    //b2.Xử lý upload
                    String realpath = request.getServletContext().getRealPath("/assets/images/products");
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    part.write(realpath + "/" + filename);
                    //b3.Thêm sản phẩm vào CSDL
                    Hoa objInsert = new Hoa(0, tenhoa, gia, filename, maloai, new Date(new java.util.Date().getTime()));
                    if (hoaDAO.Insert(objInsert)) {
                        request.setAttribute("success", "Thêm Sản phẩm thành công");
                    } else {
                        request.setAttribute("error", "Thêm Sản phẩm thất bại");
                    }
                    request.getRequestDispatcher("ManageProduct?action=list").forward(request, response);
                }
                break;

            case "edit":

                if (request.getMethod().equalsIgnoreCase("get")) {
                    //b1.Lấy mã hoa cần sửa
                    int mahoa = Integer.parseInt(request.getParameter("mahoa"));
                    request.setAttribute("hoa", hoaDAO.getById(mahoa));
                    request.setAttribute("dsLoai", loaiDAO.getAll());
                    request.getRequestDispatcher("admin/edit_product.jsp").forward(request, response);

                } else if (request.getMethod().equalsIgnoreCase("post")) {
                    int mahoa = Integer.parseInt(request.getParameter("mahoa"));
                    String tenhoa = request.getParameter("tenhoa");
                    double gia = Double.parseDouble(request.getParameter("gia"));
                    Part part = request.getPart("hinh");
                    int maloai = Integer.parseInt(request.getParameter("maloai"));
                    String filename = request.getParameter("oldImg");

                    //Xử lý upload hình ảnh
                    if (part.getSize() > 0) {
                        String realPath = request.getServletContext().getRealPath("/assets/images/products");
                        filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                        part.write(realPath + "/" + filename);
                    }
                    //b3.Cập nhật spham vào CSDL
                    Hoa objUpdate = new Hoa(mahoa, tenhoa, gia, filename, maloai, new Date(new java.util.Date().getTime()));
                    if (hoaDAO.Update(objUpdate)) {
                        request.setAttribute("success", "Cập Nhật Sản phẩm thành công");
                    } else {
                        request.setAttribute("error", "Cập Nhật Sản phẩm thất bại");
                    }
                    request.getRequestDispatcher("ManageProduct?action=list").forward(request, response);
                }
                break;
            case "delete":
                //Xử lý xóa
                int mahoa = Integer.parseInt(request.getParameter("mahoa"));

                if (hoaDAO.Delete(mahoa)) {
                    request.setAttribute("success", "Xóa Sản phẩm thành công");
                } else {
                    request.setAttribute("error", "Xóa Sản phẩm thất bại");
                }
                request.getRequestDispatcher("ManageProduct?action=list").forward(request, response);
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

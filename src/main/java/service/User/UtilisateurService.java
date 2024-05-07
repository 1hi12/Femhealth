package service.User;

import model.User.Utilisateur;
import utils.MyDataBase;
import utils.PasswordUtils;
import utils.Session;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UtilisateurService implements IService<Utilisateur> {

    Connection connection;

    public  Utilisateur user;
    public UtilisateurService() {
        connection = MyDataBase.getInstance().getConnection();
    }


    public Utilisateur afficheUser(int id) {
        Utilisateur p = new Utilisateur();
        try {
            String req = "Select * from  `user` where id=" + id;
            Statement st = connection.createStatement();
            ResultSet RS = st.executeQuery(req);
            RS.next();
            p.setId(RS.getInt("id"));
            p.setNom(RS.getString("nom"));
            p.setMail(RS.getString("email"));
            p.setMdp(RS.getString("password"));
            p.setRole(RS.getString("roles"));
            p.setRole(RS.getString("active"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }
    public Utilisateur authentification(String email) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    utilisateur = new Utilisateur();

                    utilisateur.setId(resultSet.getInt("id"));
                    utilisateur.setNom(resultSet.getString("nom"));
                    utilisateur.setMail(resultSet.getString("email"));
                    utilisateur.setMdp(resultSet.getString("password"));
                    utilisateur.setRole(resultSet.getString("roles"));
                    utilisateur.setActive(resultSet.getInt("active"));
                    Session.StartSession(utilisateur);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return utilisateur;
    }



        //class ValidationUtil {

            //public static boolean estChaineValide(String chaine) {
                // Vérifier si la chaîne est vide ou nulle
                //if (chaine == null || chaine.trim().isEmpty()) {
                    //return false;
                //}

                // Vérifier si la chaîne ne contient que des lettres
                //if (!chaine.matches("[a-zA-Z ]+")) {
                 //   return false;
                //}

                // La chaîne est valide si elle passe toutes les vérifications
                //return true;
            //}

            ////public boolean isStringLength(String str) {
               // return str.length() < 8;
            //}








    //}

    @Override
    public void add(Utilisateur utilisateur) throws SQLException {
        try {
            String req = "INSERT INTO `user` (`nom`, `email`, `password`,`roles`,`registered_at`,`active`) VALUES (?, ?, ?,?, CURRENT_TIMESTAMP, ?)";
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getMail());

            statement.setString(3, PasswordUtils.hashPasswrd(utilisateur.getMdp()));
            statement.setString(4, "ROLE_CLIENT");

            statement.setInt(5, utilisateur.getActive());


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur inséré");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }






    @Override
    public void update(Utilisateur utilisateur) throws SQLException {

        try {
            String req = "UPDATE `user` SET `nom` = ?, `email` = ?, `roles` = ?, `active` = ?, `password` = ?, `registered_at` = ? WHERE `id` = ?";
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getRole());
            statement.setInt(4, utilisateur.getActive());
            statement.setString(5, utilisateur.getMdp());

            statement.setDate(6, utilisateur.getRegistred_at());
            statement.setInt(7, utilisateur.getId());
            statement.executeUpdate();
            System.out.println("Utilisateur mis à jour");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        try {
            String req = "DELETE FROM `user` WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Utilisateur supprimé");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Utilisateur> select() throws SQLException {
        List<Utilisateur> list = new ArrayList<>();
        try {
            String req = "SELECT * FROM `user` ORDER BY id";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setActive(rs.getInt("active"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setRole(rs.getString("roles"));

                utilisateur.setRegistred_at(rs.getDate("registered_at"));
                list.add(utilisateur);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        return list;
    }

    public boolean validerMotDePasse(String email, String motDePasseActuel) {
        String query = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String motDePasseBD = resultSet.getString("password");
                    return PasswordUtils.verifyPassword(motDePasseActuel, motDePasseBD);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

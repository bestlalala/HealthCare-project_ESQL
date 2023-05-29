import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MyUser implements DB_func{
    ResultSet resultSet = null;     // 출력할 결과
    int id;
    String nickname;
    String username;
    String regist_num;
    String gender;
    Scanner sc = new Scanner(System.in);

    public void setNickname() {
        String nickname;
        while (true) {
            try {
                System.out.print("- 닉네임 (공백X, 15자 이내) : ");
                nickname = sc.next();
                if (nickname.length() <= 15) {
                    if (exist("MyUser", "nickname", nickname)) {
                        System.out.println("해당 닉네임은 사용할 수 없습니다.");
                    }
                    this.nickname = nickname;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException | SQLException e) {
                System.out.println("ERROR! 15자 이내로 입력해주세요.");
            }
        }
    }

    public boolean exist(String table, String attr, String val) throws SQLException {
        boolean result;
        ResultSet resultSet = Main.stmt.executeQuery("SELECT * FROM " + table + " WHERE " + attr + " = '" + val + "';");
        result = resultSet.next();
        return result;
    }

    public void setUsername() {
        String username;
        while (true) {
            try {
                System.out.print("- 이름 (50자 이내) : ");
                username = sc.next();
                if (username.length() <= 50) {
                    this.username = username;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR! 50자 이내로 입력해주세요.");
            }
        }

    }

    public void setRegist_num() {
        String regist_num;
        while (true) {
            try {
                System.out.print("- 주민번호 앞 6자리와 뒤 1자리 (ex. 0106274) : ");
                regist_num = sc.next();
                if (regist_num.length() == 7) {
//                int year = Integer.parseInt(regist_num.substring(0, 2));
//                int month = Integer.parseInt(regist_num.substring(2, 4));
//                int day = Integer.parseInt(regist_num.substring(4, 6));
                    int gen = Integer.parseInt(regist_num.substring(6, 7));
                    if (gen > 0 && gen < 5) {
                        this.regist_num = regist_num;
                        setGender(gen);
                        break;
                    } else {
                        System.out.println("ERROR! 주민번호가 알맞지 않습니다. 다시 입력해주세요.");
                    }
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR! 주민번호가 알맞지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    public void setGender(int g) {
        if (g == '1' || g == '3') {
            this.gender = "M";
        } else {
            this.gender = "F";
        }
    }

    public MyUser()  {
        System.out.println("===================== [ 회원 가입 ] ======================\n");
        setNickname();
        setUsername();
        setRegist_num();
        System.out.println("======================================================\n");
    }

    public MyUser(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public void select() throws SQLException {
        System.out.println("|     닉네임     |    이름    |   주민번호   | 성별 |");

        resultSet = Main.stmt.executeQuery("SELECT * FROM MyUser WHERE nickname = '" + this.nickname + "';");

        while(resultSet.next()) {
            System.out.println("| " + resultSet.getString(2) + "  |  " + resultSet.getString(3)
                    + "  |  " + resultSet.getString(4) + "  |  " + resultSet.getString(5) + "  |");
        }
    }

    public boolean login() {
        boolean result = false;
        try {
            resultSet = Main.stmt.executeQuery("SELECT * FROM MyUser WHERE nickname = '" + this.nickname + "';");
            while (resultSet.next()) {
                this.username = resultSet.getString(3);
                this.regist_num = resultSet.getString(4);
                this.gender = resultSet.getString(5);
                System.out.println("로그인 성공!");
                id = resultSet.getInt(1);
                result = true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("로그인 실패");
        }
        return result;
    }

    @Override
    public void insert() throws SQLException{
        String sql = "INSERT INTO MyUser VALUES ('" + nickname + "', '" + username + "', '" + regist_num + "', '" + gender + "');";
        System.out.println(sql);

        Main.stmt.execute(sql);
        System.out.println("회원가입 성공!");
        login();
//        resultSet = Main.stmt.executeQuery("SELECT U# FROM MyUser WHERE nickname = '" + nickname + "';");
//        id = resultSet.getInt(1);
    }

    @Override
    public void update() throws SQLException {
        System.out.println("======================================================");
        setUsername();
        System.out.println("======================================================");

        String sql = "UPDATE MyUser SET username = '" + this.username + "' WHERE nickname = '" + this.nickname +"';";

        Main.stmt.executeUpdate(sql);
        System.out.println("수정 완료");

    }

    @Override   // 회원 탈퇴 (회원 정보 삭제)
    public void delete() throws SQLException {
        String sql = "DELETE FROM MyUser " + "WHERE U# = " + this.id;
        int res = Main.stmt.executeUpdate(sql);
        if (res > 0) {
            System.out.println("회원 탈퇴가 완료되었습니다.");
        } else {
            System.out.println("회월 탈퇴를 실패하였습니다.");
        }
    }
}

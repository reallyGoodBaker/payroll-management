package service;

public class RoleFactory {
    public static Role build(int id) {
        Role role;

        switch (id) {
            case 1:
                role = new Experimentor();
                break;

            case 2:
                role = new Execs();
                break;
        
            default:
                role = new Teacher();
                break;
        }

        return role;
    }
}

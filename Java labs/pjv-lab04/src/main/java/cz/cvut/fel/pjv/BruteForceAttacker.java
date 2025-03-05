package cz.cvut.fel.pjv;

public class BruteForceAttacker extends Thief {
    
    @Override
    public void breakPassword(int sizeOfPassword) {
        char [] chars = new char[sizeOfPassword]; // pole charakteru s delkou passwordu
        char [] copy = getCharacters(); // pole pro vyzkousheni odemykani hesla a pritahuju charakters s tridy thief pomoci getteru. jsou to characters ze kterych se passw sklada
        recursionBreakPassword( chars, sizeOfPassword, copy,0); //recursion starts here

    }

    public boolean recursionBreakPassword(char [] chars, int sizeOfPassword, char [] copy, int currentIndexOfProposedPassword){
        if(currentIndexOfProposedPassword == sizeOfPassword){
            return tryOpen(chars);
        }
        for (int i = 0; i < copy.length; i++) {
            chars [currentIndexOfProposedPassword] = copy[i]; //inserts current index of some character on i-position in copy array

            if(recursionBreakPassword( chars, sizeOfPassword,  copy,  currentIndexOfProposedPassword +1)){
                return true;
            }
        }
        return false;
    }
    
}

package org.springframework.asm;

public class TypePath {

    public static final int ARRAY_ELEMENT = 0;

    public static final int INNER_TYPE = 1;

    public static final int WILDCARD_BOUND = 2;

    public static final int TYPE_ARGUMENT = 3;

    byte[] b;

    // the offset is the first byte of the type path in 'b'. 这个offset是b这个变量的第一个类型?
    int offset;

    TypePath(byte[] b,int offset){
        this.b = b;
        this.offset = offset;
    }

    public int getLength(){
        return b[offset];
    }

    public int getStep(int index){
        return b[offset + 2 * index + 1];
    }

    public int getStepArgument(int index){
        return b[offset + 2 * index + 2];
    }

    /**
     * 没看懂算法,大致意思就是要将typePaht进行转换
     */
    public static TypePath fromString(final String typePath){
        if(typePath == null || typePath.length() == 0){
            return null;
        }
        int n = typePath.length();
        ByteVector out = new ByteVector(n);
        out.putByte(0);
        for (int i = 0;i < n;){
            char c = typePath.charAt(i++);
            if(c == '['){
                out.put11(ARRAY_ELEMENT,0);
            }else if(c == '.'){
                out.put11(INNER_TYPE,0);
            }else if(c == '*'){
                out.put11(WILDCARD_BOUND,0);
            }else if(c >= '0' && c <= '9'){
                int typeArg = c - '0';
                while(i < n && (c = typePath.charAt(i)) >= '0' && c <= '9'){
                    typeArg = typeArg * 10 + c - '0';
                    i += 1;
                }
                if(i < n && typePath.charAt(i) == ';'){
                    i += 1;
                }
                out.put11(TYPE_ARGUMENT,typeArg);
            }
        }
        out.data[0] = (byte)(out.length / 2);
        return new TypePath(out.data,0);
    }


    @Override
    public String toString() {
        int length = getLength();

        StringBuilder result = new StringBuilder(length * 2);

        for (int i = 0;i < length;++i){
            switch (getStep(i)){
                case ARRAY_ELEMENT:
                    result.append('[');
                    break;
                case INNER_TYPE:
                    result.append('.');
                    break;
                case WILDCARD_BOUND:
                    result.append('*');
                    break;
                case TYPE_ARGUMENT:
                    result.append(getStepArgument(i)).append('.');
                    break;
                default:
                    result.append('_');
            }
        }
        return result.toString();
    }


}

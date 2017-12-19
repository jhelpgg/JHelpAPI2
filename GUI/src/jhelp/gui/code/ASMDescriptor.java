/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */
package jhelp.gui.code;

import java.util.regex.Pattern;
import jhelp.gui.JHelpAutoStyledTextArea;

/**
 * Descriptor of ASM language.<br>
 * It defines keywords, comments, ...<br>
 * Use {@link JHelpAutoStyledTextArea#describeLanguage(LanguageDescriptor)} to apply it
 */
public class ASMDescriptor extends LanguageDescriptor
{
    /**
     * Create the descriptor
     */
    public ASMDescriptor()
    {
        this.associate(Rules.COMMENT, Pattern.compile("//.*"), 0);
        this.associate(Rules.COMMENT, Pattern.compile("/\\*([^*]|\\*[^/]|\\n)*\\*/"), 0);
        this.associate(Rules.COMMENT, Pattern.compile(";.*"), 0);
        this.associate(Rules.STRING, Pattern.compile("\".*\""), 0);
        this.associate(Rules.STRING, Pattern.compile("'.*'"), 0);
        this.associate(Rules.PRIMITIVE, "boolean", "char", "byte", "short", "int", "long", "float", "double");
        this.associate(Rules.KEY_WORD, "this", "class", "}", "extends", "field", "field_reference", "implements",
                       "import",
                       "method", "{", "package", "parameter", "private", "protected", "public", "return", "static",
                       "throws");
        this.associate(Rules.KEY_WORD, Pattern.compile("<init>"), 0);
        this.associate(Rules.OPERAND, "AALOAD", "AASTORE", "ACONST_NULL", "ALOAD", "ANEWARRAY", "ARETURN",
                       "ARRAYLENGTH",
                       "ASTORE", "ATHROW", "BALOAD", "BASTORE", "BIPUSH", "BREAKPOINT", "CALOAD", "CASTORE",
                       "CHECKCAST",
                       "D2F", "D2I", "D2L", "DADD", "DALOAD", "DASTORE", "DCMPG", "DCMPL", "DCONST", "DDIV", "DLOAD",
                       "DMUL", "DNEG", "DREM", "DRETURN", "DSTORE", "DSUB", "DUP", "DUP_X1", "DUP_X2", "DUP2",
                       "DUP2_X1",
                       "DUP2_X2", "F2D", "F2I", "F2L", "FADD", "FALOAD", "FASTORE", "FCMPG", "FCMPL", "FCONST", "FDIV",
                       "FLOAD", "FMUL", "FNEG", "FREM", "FRETURN", "FSTORE", "FSUB", "GETFIELD", "GETSTATIC", "GOTO",
                       "GOTO_W", "I2B", "I2C", "I2D", "I2F", "I2L", "I2S", "IADD", "IALOAD", "IAND", "IASTORE",
                       "ICONST",
                       "IDIV", "IF_ACMPEQ", "IF_ACMPNE", "IF_ICMPEQ", "IF_ICMPGE", "IF_ICMPGT", "IF_ICMPLE",
                       "IF_ICMPLT",
                       "IF_ICMPNE", "IFEQ", "IFGE", "IFGT", "IFLE", "IFLT", "IFNE", "IFNONNULL", "IFNULL", "IINC",
                       "ILOAD",
                       "IMPDEP1", "IMPDEP2", "IMUL", "INEG", "INSTANCEOF", "INVOKEINTERFACE", "INVOKESPECIAL",
                       "INVOKESTATIC", "INVOKEVIRTUAL", "IOR", "IREM", "IRETURN", "ISHL", "ISHR", "ISTORE", "ISUB",
                       "IUSHR", "IXOR", "JSR", "JSR_W", "L2D", "L2F", "L2I", "LADD", "LALOAD", "LAND", "LASTORE",
                       "LCMP",
                       "LCONST", "LDC", "LDC_W", "LDC2_W", "LDIV", "LLOAD", "LMUL", "LNEG", "LOOKUPSWITCH", "LOR",
                       "LREM",
                       "LRETURN", "LSHL", "LSHR", "LSTORE", "LSUB", "LUSHR", "LXOR", "MONITORENTER", "MONITOREXIT",
                       "MULTIANEWARRAY", "NEW", "NEWARRAY", "NOP", "POP", "POP2", "PUSH", "PUTFIELD", "PUTSTATIC",
                       "RET",
                       "RETURN", "SALOAD", "SASTORE", "SIPUSH", "SWAP", "SWITCH", "TABLESWITCH", "CATCH", "LABEL",
                       "SUB_C",
                       "SUB_E", "SUB_S", "TRY", "VAR");
    }
}

/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private int commentANestLevel = 0;
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int COMMENT_B = 2;
	private final int COMMENT_A = 1;
	private final int STRING = 3;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		61,
		54,
		85
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"6:9,8,1,8:2,2,6:18,8,6,42,6:5,3,7,4,34,40,5,39,35,44:10,36,33,37,9,10,6,38," +
"45,46,47,48,49,21,46,50,51,46:2,52,46,53,54,55,46,56,57,27,58,23,59,46:3,6," +
"43,6:2,60,6,13,61,11,26,15,16,61,29,20,61:2,12,61,22,25,28,61,18,14,17,19,2" +
"4,30,61:3,31,6,32,41,6,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,170,
"0,1:3,2,3,4,1:2,5,6,7,1:6,8,1:5,9,1:4,10:2,11,10,1:2,10:15,1,12,1:2,13,1:6," +
"14,10,15,16:3,17,16:14,18,19,20,21,22,23,1,24,25,26,27,28,29,30,31,32,33,34" +
",35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59" +
",60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84" +
",85,86,10,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103")[0];

	private int yy_nxt[][] = unpackFromString(104,62,
"1,2,3,4,5,6,7,8,3,9,7,10,119,152:2,154,63,156,152:2,86,11,121,64,62,89,152," +
"153,158,152,160,12,13,14,15,16,17,18,19,20,21,22,23,7,24,64:2,155,64,157,64" +
",87,120,122,90,159,64:3,166,7,152,-1:66,25,-1:64,26,-1:59,27,-1:66,28,-1:62" +
",152,162,123,152:17,-1:13,152,123,152:6,162,152:9,-1:11,64:9,65,64:10,-1:13" +
",64:7,65,64:10,-1:5,33,-1:3,34,-1:96,24,-1:28,152:20,-1:13,152:18,-1:11,152" +
":18,146,152,-1:13,152:6,146,152:11,-1:4,52,-1:57,1,55,83:60,1,50,-1,51,82,8" +
"8:57,-1:11,152:2,131,152:6,29,152:10,-1:13,152,131,152:5,29,152:10,-1:11,64" +
":20,-1:13,64:18,-1:11,64:18,169,64,-1:13,64:6,169,64:11,-1:7,53,-1:56,83:60" +
",-1,59,-1:40,60,-1:19,1,56,-1,57:39,58,84,57:18,-1:11,152:3,137,152,30,152:" +
"4,30,31,152:8,-1:13,152:9,31,152:3,137,152:4,-1:11,64:3,167,64,66,64:4,66,6" +
"7,64:8,-1:13,64:9,67,64:3,167,64:4,-1:11,152:5,32,152:4,32,152:9,-1:13,152:" +
"18,-1:11,64:5,68,64:4,68,64:9,-1:13,64:18,-1:11,152:6,35,152:9,35,152:3,-1:" +
"13,152:18,-1:11,64:6,69,64:9,69,64:3,-1:13,64:18,-1:11,152:19,36,-1:13,152:" +
"15,36,152:2,-1:11,64:19,70,-1:13,64:15,70,64:2,-1:11,152:19,37,-1:13,152:15" +
",37,152:2,-1:11,64:19,71,-1:13,64:15,71,64:2,-1:11,152:4,38,152:15,-1:13,15" +
"2:5,38,152:12,-1:11,64:11,76,64:8,-1:13,64:9,76,64:8,-1:11,152:17,39,152:2," +
"-1:13,152:11,39,152:6,-1:11,64:4,72,64:15,-1:13,64:5,72,64:12,-1:11,152:4,4" +
"0,152:15,-1:13,152:5,40,152:12,-1:11,64:4,74,64:15,-1:13,64:5,74,64:12,-1:1" +
"1,41,152:19,-1:13,152:3,41,152:14,-1:11,75,64:19,-1:13,64:3,75,64:14,-1:11," +
"152:4,42,152:15,-1:13,152:5,42,152:12,-1:11,64:17,73,64:2,-1:13,64:11,73,64" +
":6,-1:11,152:11,43,152:8,-1:13,152:9,43,152:8,-1:11,64,77,64:18,-1:13,64:8," +
"77,64:9,-1:11,152,44,152:18,-1:13,152:8,44,152:9,-1:11,64:3,78,64:16,-1:13," +
"64:13,78,64:4,-1:11,152:3,45,152:16,-1:13,152:13,45,152:4,-1:11,64:4,79,64:" +
"15,-1:13,64:5,79,64:12,-1:11,152:4,46,152:15,-1:13,152:5,46,152:12,-1:11,64" +
":15,80,64:4,-1:13,64:4,80,64:13,-1:11,152:4,47,152:15,-1:13,152:5,47,152:12" +
",-1:11,64:3,81,64:16,-1:13,64:13,81,64:4,-1:11,152:15,48,152:4,-1:13,152:4," +
"48,152:13,-1:11,152:3,49,152:16,-1:13,152:13,49,152:4,-1:11,152:4,91,152:9," +
"125,152:5,-1:13,152:5,91,152:4,125,152:7,-1:11,64:4,92,64:9,132,64:5,-1:13," +
"64:5,92,64:4,132,64:7,-1:11,152:4,93,152:9,95,152:5,-1:13,152:5,93,152:4,95" +
",152:7,-1:11,64:4,94,64:9,96,64:5,-1:13,64:5,94,64:4,96,64:7,-1:11,152:3,97" +
",152:16,-1:13,152:13,97,152:4,-1:11,64:4,98,64:15,-1:13,64:5,98,64:12,-1:11" +
",152:14,99,152:5,-1:13,152:10,99,152:7,-1:11,64:3,100,64:16,-1:13,64:13,100" +
",64:4,-1:11,152:3,101,152:16,-1:13,152:13,101,152:4,-1:11,64:3,102,64:16,-1" +
":13,64:13,102,64:4,-1:11,152:2,103,152:17,-1:13,152,103,152:16,-1:11,64:2,1" +
"04,64:17,-1:13,64,104,64:16,-1:11,152,144,152:18,-1:13,152:8,144,152:9,-1:1" +
"1,64:14,106,64:5,-1:13,64:10,106,64:7,-1:11,152:8,105,152:11,-1:13,152:14,1" +
"05,152:3,-1:11,64:14,108,64:5,-1:13,64:10,108,64:7,-1:11,152:4,107,152:15,-" +
"1:13,152:5,107,152:12,-1:11,64:3,110,64:16,-1:13,64:13,110,64:4,-1:11,152:1" +
"2,145:2,152:6,-1:13,152:18,-1:11,64,112,64:18,-1:13,64:8,112,64:9,-1:11,152" +
":14,109,152:5,-1:13,152:10,109,152:7,-1:11,64:9,114,64:10,-1:13,64:7,114,64" +
":10,-1:11,152:9,147,152:10,-1:13,152:7,147,152:10,-1:11,64:6,116,64:9,116,6" +
"4:3,-1:13,64:18,-1:11,152:3,111,152:16,-1:13,152:13,111,152:4,-1:11,152:3,1" +
"13,152:16,-1:13,152:13,113,152:4,-1:11,152:14,148,152:5,-1:13,152:10,148,15" +
"2:7,-1:11,152:4,149,152:15,-1:13,152:5,149,152:12,-1:11,152,115,152:18,-1:1" +
"3,152:8,115,152:9,-1:11,152:9,117,152:10,-1:13,152:7,117,152:10,-1:11,152:7" +
",150,152:12,-1:13,152:12,150,152:5,-1:11,152:9,151,152:10,-1:13,152:7,151,1" +
"52:10,-1:11,152:6,118,152:9,118,152:3,-1:13,152:18,-1:11,64:18,124,64,-1:13" +
",64:6,124,64:11,-1:11,152,127,152,129,152:16,-1:13,152:8,127,152:4,129,152:" +
"4,-1:11,64,161,126,64:17,-1:13,64,126,64:6,161,64:9,-1:11,152:7,133,152:10," +
"135,152,-1:13,152:6,135,152:5,133,152:5,-1:11,64,128,64,130,64:16,-1:13,64:" +
"8,128,64:4,130,64:4,-1:11,152:14,139,152:5,-1:13,152:10,139,152:7,-1:11,64:" +
"14,134,64:5,-1:13,64:10,134,64:7,-1:11,152:18,141,152,-1:13,152:6,141,152:1" +
"1,-1:11,64:2,136,64:17,-1:13,64,136,64:16,-1:11,152:2,143,152:17,-1:13,152," +
"143,152:16,-1:11,64:9,138,64:10,-1:13,64:7,138,64:10,-1:11,64:14,140,64:5,-" +
"1:13,64:10,140,64:7,-1:11,64:9,142,64:10,-1:13,64:7,142,64:10,-1:11,64:18,1" +
"63,64,-1:13,64:6,163,64:11,-1:11,64:12,164:2,64:6,-1:13,64:18,-1:11,64:7,16" +
"5,64:12,-1:13,64:12,165,64:5,-1:11,64:4,168,64:15,-1:13,64:5,168,64:12");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
        case COMMENT_A:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case COMMENT_B:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case STRING:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
    curr_lineno++;
}
					case -3:
						break;
					case 3:
						{
    // ignore whitespace
    // not sure if \n is needed.
}
					case -4:
						break;
					case 4:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -5:
						break;
					case 5:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -10:
						break;
					case 10:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -11:
						break;
					case 11:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -16:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.LT);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.AT);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -23:
						break;
					case 23:
						{
    yybegin(STRING);
    string_buf.setLength(0);
}
					case -24:
						break;
					case 24:
						{
    AbstractSymbol symbol = AbstractTable.inttable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.INT_CONST, symbol);
}
					case -25:
						break;
					case 25:
						{
    commentANestLevel++;
    yybegin(COMMENT_A);
}
					case -26:
						break;
					case 26:
						{
   return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -27:
						break;
					case 27:
						{
    yybegin(COMMENT_B);
}
					case -28:
						break;
					case 28:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.FI);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.IF);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.IN);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.OF);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.LE);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.LET);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -50:
						break;
					case 50:
						{
    curr_lineno++;
}
					case -51:
						break;
					case 51:
						{
}
					case -52:
						break;
					case 52:
						{
    commentANestLevel++;
}
					case -53:
						break;
					case 53:
						{
    commentANestLevel--;
    if (commentANestLevel == 0) {
        yybegin(YYINITIAL);
    } else if (commentANestLevel < 0) {
        return new Symbol(TokenConstants.ERROR, "Unmatched *)");
    }
}
					case -54:
						break;
					case 54:
						{
}
					case -55:
						break;
					case 55:
						{
    curr_lineno++;
    yybegin(YYINITIAL);
}
					case -56:
						break;
					case 56:
						{
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -57:
						break;
					case 57:
						{
    string_buf.append(yytext());
}
					case -58:
						break;
					case 58:
						{
    yybegin(YYINITIAL);
    String str = string_buf.toString();
    StringBuffer newBuffer = new StringBuffer();
    char[] mappableCharacters = { 'n', 'b', 't', 'f' };
    char[] constantCharacters = { '\n', '\b', '\t', '\f' };
    boolean isPreviousBackslashEscaped = false;
    for (char c : str.toCharArray()) {
        Character previousCharacter = null;
        if (newBuffer.length() > 0) {
            previousCharacter = newBuffer.charAt(newBuffer.length() - 1);
        }
		boolean previousCharacterIsBackslash = previousCharacter != null && previousCharacter == '\\';
        if (c == '\\') {
            if (!isPreviousBackslashEscaped && previousCharacterIsBackslash) {
                isPreviousBackslashEscaped = true;
            } else {
                newBuffer.append('\\');
                isPreviousBackslashEscaped = false;
            }
        } else {
            boolean replaced = false;
            for (int i = 0; i < mappableCharacters.length; i++) {
                char mappableCharacter = mappableCharacters[i];
                if (!isPreviousBackslashEscaped && previousCharacterIsBackslash && mappableCharacter == c) { 
                    newBuffer.setLength(newBuffer.length() - 1);
                    newBuffer.append(constantCharacters[i]);
                    replaced = true;
                    break;
                }
            }
            if (replaced) {
				continue;
            }
            // catch unescaped null characters
            if (!previousCharacterIsBackslash || isPreviousBackslashEscaped) {
                if (c == '\0') {
                    return new Symbol(TokenConstants.ERROR, "String contains null character");
                }
            }
            // remove previous backslash such that \c -> c.
			if (!isPreviousBackslashEscaped && previousCharacterIsBackslash) {
				newBuffer.setLength(newBuffer.length() - 1);	
			}
			newBuffer.append(c);
        }
    }
    String newStr = newBuffer.toString();
    AbstractSymbol symbol = AbstractTable.stringtable.addString(newStr, newStr.length());
    return new Symbol(TokenConstants.STR_CONST, symbol);
}
					case -59:
						break;
					case 59:
						{
    // escaped newline
    curr_lineno++;
    string_buf.append(yytext());
}
					case -60:
						break;
					case 60:
						{
   string_buf.append('"');
}
					case -61:
						break;
					case 62:
						{
    // ignore whitespace
    // not sure if \n is needed.
}
					case -62:
						break;
					case 63:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -63:
						break;
					case 64:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.FI);
}
					case -65:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.IF);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.IN);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.OF);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.LET);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -78:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -79:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -80:
						break;
					case 81:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -81:
						break;
					case 82:
						{
}
					case -82:
						break;
					case 83:
						{
}
					case -83:
						break;
					case 84:
						{
    string_buf.append(yytext());
}
					case -84:
						break;
					case 86:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -85:
						break;
					case 87:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -86:
						break;
					case 88:
						{
}
					case -87:
						break;
					case 89:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -88:
						break;
					case 90:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -89:
						break;
					case 91:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -90:
						break;
					case 92:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -91:
						break;
					case 93:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -92:
						break;
					case 94:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -93:
						break;
					case 95:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -94:
						break;
					case 96:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -95:
						break;
					case 97:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -96:
						break;
					case 98:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -97:
						break;
					case 99:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -98:
						break;
					case 100:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -99:
						break;
					case 101:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -100:
						break;
					case 102:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -101:
						break;
					case 103:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -102:
						break;
					case 104:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -103:
						break;
					case 105:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -104:
						break;
					case 106:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -105:
						break;
					case 107:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -106:
						break;
					case 108:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -107:
						break;
					case 109:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -108:
						break;
					case 110:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -109:
						break;
					case 111:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -110:
						break;
					case 112:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -111:
						break;
					case 113:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -112:
						break;
					case 114:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -113:
						break;
					case 115:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -114:
						break;
					case 116:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -115:
						break;
					case 117:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -116:
						break;
					case 118:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -117:
						break;
					case 119:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -118:
						break;
					case 120:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -119:
						break;
					case 121:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -120:
						break;
					case 122:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -121:
						break;
					case 123:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -122:
						break;
					case 124:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -123:
						break;
					case 125:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -124:
						break;
					case 126:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -125:
						break;
					case 127:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -126:
						break;
					case 128:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -127:
						break;
					case 129:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -128:
						break;
					case 130:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -129:
						break;
					case 131:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -130:
						break;
					case 132:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -131:
						break;
					case 133:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -132:
						break;
					case 134:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -133:
						break;
					case 135:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -134:
						break;
					case 136:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -135:
						break;
					case 137:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -136:
						break;
					case 138:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -137:
						break;
					case 139:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -138:
						break;
					case 140:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -139:
						break;
					case 141:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -140:
						break;
					case 142:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -141:
						break;
					case 143:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -142:
						break;
					case 144:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -143:
						break;
					case 145:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -144:
						break;
					case 146:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -145:
						break;
					case 147:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -146:
						break;
					case 148:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -147:
						break;
					case 149:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -148:
						break;
					case 150:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -149:
						break;
					case 151:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -150:
						break;
					case 152:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -151:
						break;
					case 153:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -152:
						break;
					case 154:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -153:
						break;
					case 155:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -154:
						break;
					case 156:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -155:
						break;
					case 157:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -156:
						break;
					case 158:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -157:
						break;
					case 159:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -158:
						break;
					case 160:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -159:
						break;
					case 161:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -160:
						break;
					case 162:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -161:
						break;
					case 163:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -162:
						break;
					case 164:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -163:
						break;
					case 165:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -164:
						break;
					case 166:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -165:
						break;
					case 167:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -166:
						break;
					case 168:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -167:
						break;
					case 169:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -168:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}

package ru.crazylegend.focus.util.math.progress;

class ProgressFormatImpl implements ProgressFormat {

    private int size;
    private String yes, no;

    ProgressFormatImpl(int size, String yes, String no) {
        this.size = size;
        this.yes = yes;
        this.no = no;
    }

    @Override
    public String format(ProgressState state) {
        final StringBuilder builder = new StringBuilder();
        int yesCount = (int) (state.getCurrent() / (state.getMaximum() / size));
        for (int i = 1; i <= size; i++, yesCount--) {
            builder.append(yesCount > 0 ? yes : no);
        }
        return builder.toString();
    }
}

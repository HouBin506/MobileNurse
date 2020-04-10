function btn1_OnClick() {
    var mark1 = parseInt(getProperty("te1", "Value"));
    var mark2 = parseInt(getProperty("te2", "Value"));

    var total = (mark1 + mark2).toString();
    close(total);
}
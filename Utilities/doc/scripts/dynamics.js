function clickOn(action)
{
    var start = 0;
    var length = action.length;
    var index = action.indexOf(';');

    while(index > 0 && start < length)
    {
        oneAction(action.substring(start, index));
        start = index + 1;
        index = action.indexOf(';', start);
    }

    if(start < length)
    {
        oneAction(action.substring(start));
    }
}

function oneAction(action)
{
    var index = action.indexOf(':');
    var actionName = action.substring(0, index);
    var actionParameter = action.substring(index + 1);

    switch(actionName)
    {
        case "ENABLE" :
            document.getElementById(actionParameter).disabled = false;
        break;
        case "DISABLE" :
            document.getElementById(actionParameter).disabled = true;
        break;
    }
}
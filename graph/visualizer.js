function showGraph(text) {
    document.getElementById('graph').innerHTML = ''

    var lines = text.split('\n')
    var filesNum = parseInt(lines[0].split(' ')[0])
    var graph = {}
    for (var i = 0; i < filesNum; i++) {
        var line = lines[i * 2 + 1];
        var name = line.split(' ')[0];
        var deps = lines[i * 2 + 2].split(' ').slice(1);
        graph[name] = deps
    }

    var visualGraph = Viva.Graph.graph();

    var targetIndex = filesNum * 2 + 1;
    var targetNum = 4

    function add(name) {
        var deps = graph[name]
        deps.forEach((it) => {
            visualGraph.addLink(name, it)
            add(it)
        })
    }
    var targets = [
        lines[targetIndex + targetNum].split(' ')[0],
        lines[targetIndex + targetNum - 1].split(' ')[0],
        lines[targetIndex + targetNum - 2].split(' ')[0],
        lines[targetIndex + targetNum - 3].split(' ')[0],
        lines[targetIndex + targetNum - 4].split(' ')[0],
    ]
    targets.forEach((it) => add(it))

    // specify where it should be rendered:
    var graphics = Viva.Graph.View.webglGraphics();
    graphics.node((node) => (targets.indexOf(node.id) != -1) ? Viva.Graph.View.webglSquare(10, '#ff0000') : Viva.Graph.View.webglSquare(10, '#00ff00'))

    var renderer = Viva.Graph.View.renderer(visualGraph, {
        graphics : graphics,
        container: document.getElementById('graph')
    });
    renderer.run();
}

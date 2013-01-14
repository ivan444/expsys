/*
 * Visualisation of fuzzy sets for one characteristic.
 */

function flatten(arr) {
  var isArray = function(obj) {
      return Object.prototype.toString.call(obj) === '[object Array]';
  }

  var flat = [];
  for ( var i = 0; i < arr.length; i++) {
    if (isArray(arr[i])) {
      flat = flat.concat(flatten(arr[i]));
    } else {
      flat.push(arr[i]);
    }
  }
  
  return flat;
}

function zipxs(xs, ys, xpad, ypad, ymax) {
  var zip = [];
  for (var i = 0; i < xs.length; i++) {
    zip.push({
      x : xs[i]+xpad,
      y : (i%3 == 0 ? ymax : 0)+ypad
    });
  }
  return zip;
}

function showFuzzyClasses(divId, chr) {
  var fuzzyClasses = undefined;
  var xTicks = [];
  var data = [];
  var xpad = 32;
  var ypad = 15;
  var ymax = 100;
  var xmax = 600;
  var width = 700,
    height = 180,
    strokeW = 1.5,
    circR = 5,
    xTickR = 2;
  var circHalfW = circR + strokeW + xpad;
  var circRS = circR + strokeW;
  var ys = [ymax+ypad, ypad, ypad, ymax+ypad];
  
  fuzzyClasses = chr.fcls;
  var colors = ["red", "blue", "green", "yellow", "orange", "purple", "teal", "violet", "grey", "olive", "brown"];
  
  for (var i = 0; i < fuzzyClasses.length; i++) {
    var fsi = fuzzyClasses[i];
    for (var j = 0; j < 4; j++) {
      data.push({x: fsi.xs[j]+xpad, y: (j%3 == 0 ? ymax : 0)+ypad, idx: i, iidx: j});
    }
  }
  
  for (var i = 0; i < chr.chrValues.length; i++) {
    xTicks.push({x: chr.chrValues[i].x + xpad, text: chr.chrValues[i].val, idx: i });
  }
  
  
  var svgDiv = document.getElementById(divId);
  var svg = d3.select(svgDiv).append("svg")
  .attr("width", width)
  .attr("height", height);
  
  var drag = d3.behavior.drag()
    .origin(Object)
    .on("drag", function (d) {
      var cx = Math.max(circHalfW, Math.min(width - circHalfW, d3.event.x));
      
      var oldCx = fuzzyClasses[d.idx].xs[d.iidx]+xpad;
      var goLeft = cx - oldCx < 0;
      var goRight = cx - oldCx > 0;
      var canMoveLeft = goLeft && (d.iidx == 0 || cx > fuzzyClasses[d.idx].xs[d.iidx-1]+xpad);
      var canMoveRight = goRight && (d.iidx == 3 || cx < fuzzyClasses[d.idx].xs[d.iidx+1]+xpad);
      if (canMoveLeft || canMoveRight) {
        fuzzyClasses[d.idx].xs[d.iidx] = cx-xpad;
        d.x = cx;
        updateAreas();
        updateLines();
        d3.select(this).attr("cx", cx);
      }
      
//      console.log((canMoveLeft || canMoveRight) + ", " + fuzzyClasses[d.idx].xs[d.iidx]);
    });
  
  var drawLines = function() {
    svg.selectAll(".borders").data(fuzzyClasses).enter()
    .append("g")
      .attr("stroke", "orange")
      .attr("stroke-width", strokeW)
      .attr("fill", "lightgreen")
      .attr("class", "borders")
      .call(function (g) {
        g.append("line")
          .attr("x1", function (d) { return d.xs[0]+xpad; })
          .attr("y1", ypad+ymax)
          .attr("x2", function (d) { return d.xs[1]+xpad; })
          .attr("y2", ypad);
        g.append("line")
          .attr("x1", function (d) { return d.xs[1]+xpad; })
          .attr("y1", ypad)
          .attr("x2", function (d) { return d.xs[2]+xpad; })
          .attr("y2", ypad);
        g.append("line")
          .attr("x1", function (d) { return d.xs[2]+xpad; })
          .attr("y1", ypad)
          .attr("x2", function (d) { return d.xs[3]+xpad; })
          .attr("y2", ypad+ymax);
    });
  };
  
  var updateLines = function() {
    var lines = svg.selectAll(".borders").selectAll("line");
    lines = flatten(lines);
    var padd = xpad;
    for (var i = 0; i < fuzzyClasses.length; i++) {
      lines[i*3].setAttribute("x1", fuzzyClasses[i].xs[0]+padd);
      lines[i*3].setAttribute("x2", fuzzyClasses[i].xs[1]+padd);
      lines[i*3+1].setAttribute("x1", fuzzyClasses[i].xs[1]+padd);
      lines[i*3+1].setAttribute("x2", fuzzyClasses[i].xs[2]+padd);
      lines[i*3+2].setAttribute("x1", fuzzyClasses[i].xs[2]+padd);
      lines[i*3+2].setAttribute("x2", fuzzyClasses[i].xs[3]+padd);
    }
  };
  
  var drawCircles = function() {
    svg.selectAll(".dragCircle").data(data).enter()
      .append("g")
        .attr("stroke", "orange")
        .attr("stroke-width", strokeW)
        .attr("fill", "lightgreen")
        .append("circle")
          .attr("cx", function(d) { return d.x; })
          .attr("cy", function(d) { return d.y; })
          .attr("r", circR)
          .call(drag);
  };
  
  var areaLineGen = d3.svg.line()
    .x(function(d){return d.x;})
    .y(function(d){return d.y;})
    .interpolate("linear");

  var drawAreas = function() {
    for (var i = 0; i < fuzzyClasses.length; i++) {
      var fsi = fuzzyClasses[i];
      svg.append("path")
        .attr("d", areaLineGen(zipxs(fsi.xs, ys, xpad, ypad, ymax)))
        .style("stroke-width", 0)
        .style("fill", colors[i+5])
        .style("opacity", "0.25");
    }
  };
  
  var updateAreas = function() {
    var paths = svg.selectAll("path");
    var pData = [];
    for (var i = 0; i < fuzzyClasses.length; i++) {
      var fsi = fuzzyClasses[i];
      pData.push(zipxs(fsi.xs, ys, xpad, ypad, ymax));
    }
    paths.data(pData).attr("d", areaLineGen);
  };
  
  var drawAxis = function(yticks, xNamedVals) {
    var g = svg.append("g")
     .attr("stroke", "black")
     .attr("stroke-width", 1);
    
    // x-axis
    g.append("line")
      .attr("x1", xpad)
      .attr("y1", ypad+ymax)
      .attr("x2", xpad+xmax)
      .attr("y2", ypad+ymax);
    
    // y-axis
    g.append("line")
      .attr("x1", xpad)
      .attr("y1", ypad+ymax)
      .attr("x2", xpad)
      .attr("y2", ypad);
    
    // ticks and labels
    var yAxGen = d3.scale.linear().domain([0,yticks.length-1]).range([ypad+ymax,ypad]);
    var yTicksData = yticks.map(function(d,i){ return {x1:xpad, y1:yAxGen(i), x2:xpad-5, y2:yAxGen(i), tick:d}; });
    g.selectAll(".yTicks").data(yTicksData).enter()
      .append("line")
        .attr("x1", function(d){ return d.x1; })
        .attr("y1", function(d){ return d.y1; })
        .attr("x2", function(d){ return d.x2; })
        .attr("y2", function(d){ return d.y2; })
        .attr("class", "yTicks");
    
     g.selectAll(".yTicksName").data(yTicksData).enter()
      .append("text")
        .text(function(d){ return d.tick.toFixed(2); })
        .attr("x", function(d){ return d.x1; })
        .attr("y", function(d){ return d.y1; })
        .attr("dy", 3)
        .attr("dx", -31)
        .attr("class", "yTicksName");
    
    // top line
    g.append("g")
     .attr("stroke", "lightgray")
     .attr("stroke-width", 0.5)
     .append("line")
        .attr("x1", xpad)
        .attr("y1", ypad)
        .attr("x2", xpad+xmax)
        .attr("y2", ypad);
  };
  
  var drawCharacteristicName = function(chrName) {
    svg.append('text').text(chrName)
      .attr('x', xpad)
      .attr('y', ypad-5)
      .attr('fill', 'black');  
  };
  
  var dragValues = d3.behavior.drag()
    .origin(Object)
    .on("drag", function (d) {
      var x = Math.max(d3.event.x, xpad);
      
      d.x = x;
      d3.select(this).attr("transform", function(d) { return 'translate(' + x + ',0)'; });
    });

  var drawValues = function(vals) {
    var g = svg.selectAll(".xTicks")
      .data(vals).enter().append("g")
        .attr("transform", function(d) { return "translate(" + d.x + ", 0)"; })
        .attr("fill", "black")
        .call(dragValues);
      
    g.append("circle")
      .attr("cx", function(d) { return 0; })
      .attr("cy", function(d) { return ypad+ymax; })
      .attr("r", xTickR);
      
    g.append("text")
      .text(function(d) { return d.text; })
        .attr('text-anchor', 'end')
        .attr('transform', function(d) { return 'translate(' + 0 + ',' + (ymax+ypad+10) + ') rotate(-45)'; });
  };
  
  drawAxis([0.0, 0.25, 0.5, 0.75, 1.0],
      [{tick:"a", val:100}, {tick:"b", val:180}, {tick:"c", val:300}, {tick:"d", val:500}]);
  drawAreas();
  drawValues(xTicks);
  drawLines();
  drawCircles();
  drawCharacteristicName(chr.chrName);
}

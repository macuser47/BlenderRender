import bpy
import sys

bpy.context.scene.render.filepath = "C:\\Users\\fred\\Documents\\GitHub\\BlenderRender\\merp.png"#sys.argv[1]
bpy.ops.render.render( write_still=True )